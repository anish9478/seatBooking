package com.anish.seatbooking.exception;

public class BookingConflictException extends RuntimeException{
    public BookingConflictException(String message) {
        super(message);
    }
}
