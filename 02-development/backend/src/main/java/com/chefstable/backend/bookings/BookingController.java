package com.chefstable.backend.bookings;

import com.chefstable.backend.bookings.dto.BookingListResponse;
import com.chefstable.backend.bookings.dto.BookingResponse;
import com.chefstable.backend.bookings.dto.CreateBookingRequest;
import com.chefstable.backend.bookings.dto.UpdateBookingRequest;
import com.chefstable.backend.common.CurrentClient;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CurrentClient currentClient;

    public BookingController(BookingService bookingService, CurrentClient currentClient) {
        this.bookingService = bookingService;
        this.currentClient = currentClient;
    }

    @GetMapping
    BookingListResponse getBookings(@RequestParam(name = "status", required = false) String status) {
        return bookingService.getBookings(currentClient.id(), status);
    }

    @PostMapping
    ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(currentClient.id(), request));
    }

    @GetMapping("/{bookingId}")
    BookingResponse getBooking(@PathVariable String bookingId) {
        return bookingService.getBooking(currentClient.id(), bookingId);
    }

    @PutMapping("/{bookingId}")
    BookingResponse updateBooking(@PathVariable String bookingId, @Valid @RequestBody UpdateBookingRequest request) {
        return bookingService.updateBooking(currentClient.id(), bookingId, request);
    }

    @PostMapping("/studio/cancel-class/{classId}")
    ResponseEntity<BookingListResponse> cancelClassByStudio(@PathVariable String classId) {
        return ResponseEntity.ok(bookingService.cancelClassByStudio(classId));
    }
}
