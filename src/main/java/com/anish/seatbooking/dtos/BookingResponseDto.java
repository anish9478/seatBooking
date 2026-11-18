package com.anish.seatbooking.dtos;

import java.time.LocalDateTime;

public class BookingResponseDto {
    private Long bookingId;
    private String username;
    private String seatNumber;
    private LocalDateTime bookingTime;
    private String status;

    public BookingResponseDto(Long bookingId, String username, String seatNumber, LocalDateTime bookingTime, String status){
        this.bookingId = bookingId;
        this.username = username;
        this.seatNumber = seatNumber;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getUsername() {
        return username;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
}
