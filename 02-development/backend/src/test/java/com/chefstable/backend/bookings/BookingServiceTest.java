package com.chefstable.backend.bookings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.chefstable.backend.chefs.ChefMapper;
import com.chefstable.backend.classes.CookingClassMapper;
import com.chefstable.backend.common.ConflictException;
import com.chefstable.backend.common.IdGenerator;
import com.chefstable.backend.domain.entity.BookingEntity;
import com.chefstable.backend.domain.entity.ChefEntity;
import com.chefstable.backend.domain.entity.ClientEntity;
import com.chefstable.backend.domain.entity.CookingClassEntity;
import com.chefstable.backend.domain.entity.RentalPackageEntity;
import com.chefstable.backend.domain.model.BookingStatus;
import com.chefstable.backend.domain.model.CookingClassStatus;
import com.chefstable.backend.domain.model.RentalPackageName;
import com.chefstable.backend.domain.repository.BookingRepository;
import com.chefstable.backend.domain.repository.ClientRepository;
import com.chefstable.backend.domain.repository.CookingClassRepository;
import com.chefstable.backend.domain.repository.RentalPackageRepository;
import com.chefstable.backend.profile.ClientMapper;
import com.chefstable.backend.rental.RentalPackageMapper;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class BookingServiceTest {

    private final BookingRepository bookingRepository = org.mockito.Mockito.mock(BookingRepository.class);
    private final ClientRepository clientRepository = org.mockito.Mockito.mock(ClientRepository.class);
    private final CookingClassRepository cookingClassRepository = org.mockito.Mockito.mock(CookingClassRepository.class);
    private final RentalPackageRepository rentalPackageRepository = org.mockito.Mockito.mock(RentalPackageRepository.class);
    private final BookingService bookingService = new BookingService(
            bookingRepository,
            clientRepository,
            cookingClassRepository,
            rentalPackageRepository,
            new BookingMapper(new ClientMapper(), new CookingClassMapper(new ChefMapper()), new RentalPackageMapper()),
            new IdGenerator()
    );

    @Test
    void createBookingRejectsClassWithoutSeats() {
        ClientEntity client = new ClientEntity("client_1", "Ivan", "ivan@example.com", "+79990000000", "hash", OffsetDateTime.now());
        CookingClassEntity cookingClass = cookingClass("class_1", 0, OffsetDateTime.now().plusDays(1));
        RentalPackageEntity rentalPackage = rentalPackage();

        when(clientRepository.findById("client_1")).thenReturn(Optional.of(client));
        when(cookingClassRepository.findByIdForUpdate("class_1")).thenReturn(Optional.of(cookingClass));
        when(rentalPackageRepository.findById("pkg_basic")).thenReturn(Optional.of(rentalPackage));

        assertThatThrownBy(() -> bookingService.createBooking("client_1", new com.chefstable.backend.bookings.dto.CreateBookingRequest("class_1", "pkg_basic", null)))
                .isInstanceOf(ConflictException.class)
                .hasMessage("Class has no available seats");
    }

    @Test
    void lateCancellationAddsPenaltyPoint() {
        ClientEntity client = new ClientEntity("client_1", "Ivan", "ivan@example.com", "+79990000000", "hash", OffsetDateTime.now());
        CookingClassEntity cookingClass = cookingClass("class_1", 1, OffsetDateTime.now().plusHours(2));
        BookingEntity booking = new BookingEntity("booking_1", client, cookingClass, rentalPackage(), null, OffsetDateTime.now());

        when(bookingRepository.findDetailedByIdAndClientId("booking_1", "client_1")).thenReturn(Optional.of(booking));

        var response = bookingService.updateBooking("client_1", "booking_1", new com.chefstable.backend.bookings.dto.UpdateBookingRequest("cancel", null));

        assertThat(response.status()).isEqualTo(BookingStatus.CANCELLED_BY_CLIENT.name());
        assertThat(response.penaltyPoints()).isEqualTo(1);
    }

    private CookingClassEntity cookingClass(String id, int availableSeats, OffsetDateTime dateTime) {
        CookingClassEntity cookingClass = instantiate(CookingClassEntity.class);
        ReflectionTestUtils.setField(cookingClass, "id", id);
        ReflectionTestUtils.setField(cookingClass, "title", "Pasta");
        ReflectionTestUtils.setField(cookingClass, "description", "Class description");
        ReflectionTestUtils.setField(cookingClass, "dateTime", dateTime);
        ReflectionTestUtils.setField(cookingClass, "duration", 3);
        ReflectionTestUtils.setField(cookingClass, "maxParticipants", 12);
        ReflectionTestUtils.setField(cookingClass, "availableSeats", availableSeats);
        ReflectionTestUtils.setField(cookingClass, "chef", chef());
        ReflectionTestUtils.setField(cookingClass, "classType", "novice");
        ReflectionTestUtils.setField(cookingClass, "price", BigDecimal.valueOf(2500));
        ReflectionTestUtils.setField(cookingClass, "status", CookingClassStatus.SCHEDULED);
        return cookingClass;
    }

    private ChefEntity chef() {
        ChefEntity chef = instantiate(ChefEntity.class);
        ReflectionTestUtils.setField(chef, "id", "chef_1");
        ReflectionTestUtils.setField(chef, "name", "Anna");
        ReflectionTestUtils.setField(chef, "specialization", "Italian");
        ReflectionTestUtils.setField(chef, "rating", BigDecimal.valueOf(4.8));
        ReflectionTestUtils.setField(chef, "totalReviews", 10);
        return chef;
    }

    private RentalPackageEntity rentalPackage() {
        RentalPackageEntity rentalPackage = instantiate(RentalPackageEntity.class);
        ReflectionTestUtils.setField(rentalPackage, "id", "pkg_basic");
        ReflectionTestUtils.setField(rentalPackage, "packageName", RentalPackageName.Basic);
        ReflectionTestUtils.setField(rentalPackage, "description", "Apron");
        ReflectionTestUtils.setField(rentalPackage, "price", BigDecimal.valueOf(300));
        ReflectionTestUtils.setField(rentalPackage, "availableCount", 10);
        return rentalPackage;
    }

    private <T> T instantiate(Class<T> type) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
