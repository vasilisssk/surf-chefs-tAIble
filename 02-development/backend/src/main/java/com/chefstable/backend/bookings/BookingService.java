package com.chefstable.backend.bookings;

import com.chefstable.backend.bookings.dto.BookingListResponse;
import com.chefstable.backend.bookings.dto.BookingResponse;
import com.chefstable.backend.bookings.dto.CreateBookingRequest;
import com.chefstable.backend.bookings.dto.UpdateBookingRequest;
import com.chefstable.backend.common.ConflictException;
import com.chefstable.backend.common.IdGenerator;
import com.chefstable.backend.common.NotFoundException;
import com.chefstable.backend.common.ValidationException;
import com.chefstable.backend.domain.entity.BookingEntity;
import com.chefstable.backend.domain.entity.ClientEntity;
import com.chefstable.backend.domain.entity.CookingClassEntity;
import com.chefstable.backend.domain.entity.RentalPackageEntity;
import com.chefstable.backend.domain.model.BookingStatus;
import com.chefstable.backend.domain.repository.BookingRepository;
import com.chefstable.backend.domain.repository.ClientRepository;
import com.chefstable.backend.domain.repository.CookingClassRepository;
import com.chefstable.backend.domain.repository.RentalPackageRepository;
import com.chefstable.backend.domain.model.CookingClassStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private static final int FREE_CANCELLATION_HOURS = 24;

    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final CookingClassRepository cookingClassRepository;
    private final RentalPackageRepository rentalPackageRepository;
    private final BookingMapper bookingMapper;
    private final IdGenerator idGenerator;

    public BookingService(
            BookingRepository bookingRepository,
            ClientRepository clientRepository,
            CookingClassRepository cookingClassRepository,
            RentalPackageRepository rentalPackageRepository,
            BookingMapper bookingMapper,
            IdGenerator idGenerator
    ) {
        this.bookingRepository = bookingRepository;
        this.clientRepository = clientRepository;
        this.cookingClassRepository = cookingClassRepository;
        this.rentalPackageRepository = rentalPackageRepository;
        this.bookingMapper = bookingMapper;
        this.idGenerator = idGenerator;
    }

    @Transactional(readOnly = true)
    public BookingListResponse getBookings(String clientId, String statusFilter) {
        OffsetDateTime now = OffsetDateTime.now();
        List<BookingResponse> bookings = bookingRepository.findDetailedByClientId(clientId)
                .stream()
                .filter(booking -> matchesStatusFilter(booking, statusFilter, now))
                .map(bookingMapper::toResponse)
                .toList();
        return new BookingListResponse(bookings);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBooking(String clientId, String bookingId) {
        return bookingRepository.findDetailedByIdAndClientId(bookingId, clientId)
                .map(bookingMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    @Transactional
    public BookingResponse createBooking(String clientId, CreateBookingRequest request) {
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found"));
        CookingClassEntity cookingClass = cookingClassRepository.findByIdForUpdate(request.classId())
                .orElseThrow(() -> new NotFoundException("Class not found"));
        RentalPackageEntity rentalPackage = rentalPackageRepository.findById(request.rentalPackageId())
                .orElseThrow(() -> new NotFoundException("Rental package not found"));
        if (cookingClass.getStatus() == CookingClassStatus.CANCELLED) {
            throw new ConflictException("Booking is forbidden: class has been cancelled by the studio");
        }
        if (!cookingClass.canBeBooked()) {
            throw new ConflictException("Class has no available seats");
        }
        if (bookingRepository.existsByClientIdAndCookingClassId(clientId, cookingClass.getId())) {
            throw new ConflictException("Client already has a booking for this class");
        }
        cookingClass.reserveSeat();
        BigDecimal totalPrice = cookingClass.getPrice().add(rentalPackage.getPrice());
        BookingEntity booking = new BookingEntity(
                idGenerator.prefixed("booking"),
                client,
                cookingClass,
                rentalPackage,
                request.allergies(),
                OffsetDateTime.now(),
                totalPrice
        );
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse updateBooking(String clientId, String bookingId, UpdateBookingRequest request) {
        if (!"cancel".equals(request.action())) {
            throw new ValidationException("Only cancel action is supported");
        }
        BookingEntity booking = bookingRepository.findDetailedByIdAndClientId(bookingId, clientId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new ValidationException("Only confirmed bookings can be cancelled");
        }
        OffsetDateTime now = OffsetDateTime.now();
        boolean lateCancellation = now.plusHours(FREE_CANCELLATION_HOURS).isAfter(booking.getCookingClass().getDateTime());
        booking.cancelByClient(now, lateCancellation);
        booking.getCookingClass().releaseSeat();
        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public BookingListResponse cancelClassByStudio(String classId) {
        CookingClassEntity cookingClass = cookingClassRepository.findByIdForUpdate(classId)
                .orElseThrow(() -> new NotFoundException("Class not found"));
        if (cookingClass.getStatus() == CookingClassStatus.CANCELLED) {
            throw new ConflictException("Class is already cancelled");
        }
        OffsetDateTime now = OffsetDateTime.now();
        List<BookingEntity> bookings = bookingRepository.findAllConfirmedByClassId(classId);
        bookings.forEach(booking -> booking.cancelByStudio(now));
        cookingClass.cancelByStudio();
        List<BookingResponse> responses = bookings.stream().map(bookingMapper::toResponse).toList();
        return new BookingListResponse(responses);
    }

    private boolean matchesStatusFilter(BookingEntity booking, String statusFilter, OffsetDateTime now) {
        if (statusFilter == null || "all".equals(statusFilter)) {
            return true;
        }
        if ("upcoming".equals(statusFilter)) {
            return booking.getStatus() == BookingStatus.CONFIRMED && booking.getCookingClass().getDateTime().isAfter(now);
        }
        if ("past".equals(statusFilter)) {
            return booking.getCookingClass().getDateTime().isBefore(now);
        }
        if ("cancelled".equals(statusFilter)) {
            return booking.getStatus() == BookingStatus.CANCELLED_BY_CLIENT || booking.getStatus() == BookingStatus.CANCELLED_BY_STUDIO;
        }
        throw new ValidationException("Unsupported booking status filter");
    }
}
