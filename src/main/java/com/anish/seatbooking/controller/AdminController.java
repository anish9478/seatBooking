package com.anish.seatbooking.controller;

import com.anish.seatbooking.dtos.AdminDashboardResponse;
import com.anish.seatbooking.dtos.BookingResponseDto;
import com.anish.seatbooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private BookingService bookingService;
    @GetMapping("/dashboard")
    public AdminDashboardResponse getAdminStats() {
        return bookingService.getDashboardStats();
    }
    @GetMapping("/bookings")
    public List<BookingResponseDto> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
