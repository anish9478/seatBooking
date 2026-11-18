package com.anish.seatbooking.controller;

import com.anish.seatbooking.dtos.BookingResponseDto;
import com.anish.seatbooking.entity.Booking;
import com.anish.seatbooking.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/{seatId}")
    public Map<String , Object> bookSeat(@PathVariable Long seatId, Authentication authentication) {
        String username = authentication.getName();
        Booking booking = bookingService.seatBook(seatId, username);
        return Map.of("message", "seat booked successfully",
                "bookingId", booking.getId(),
                "seatId", booking.getSeat().getId(),
                "username", booking.getUser().getUsername(),
                "bookingTime", booking.getBookingTime());
    }
    @GetMapping("/my")
    public List<BookingResponseDto> getMyBookings(Authentication authentication) {
        String username = authentication.getName();
        return bookingService.getMyBookings(username);
    }
    @DeleteMapping("/{bookingId}")
    public Map<String, String> deleteBooking(@PathVariable Long bookingId, Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication
                .getAuthorities()
                .stream().
                anyMatch(authority -> authority.getAuthority()
                        .equals("ROLE_ADMIN"));
        bookingService.cancelBooking(bookingId, username, isAdmin);
        return Map.of("message", "Booking cancelled successfully");
    }

}
