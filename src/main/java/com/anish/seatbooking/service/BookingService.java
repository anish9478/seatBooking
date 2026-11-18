package com.anish.seatbooking.service;

import com.anish.seatbooking.dtos.AdminDashboardResponse;
import com.anish.seatbooking.dtos.BookingResponseDto;
import com.anish.seatbooking.entity.Booking;
import com.anish.seatbooking.entity.Seat;
import com.anish.seatbooking.entity.User;
import com.anish.seatbooking.exception.*;
import com.anish.seatbooking.repository.BookingRepository;
import com.anish.seatbooking.repository.SeatRepository;
import com.anish.seatbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public Booking seatBook(Long seatId, String username) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new ResourceNotFoundException("seat not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        if(seat.isBooked()) {
            throw new SeatAlreadyBookedException("Seat is already booked");
        }
        seat.setBooked(true);
        try{
            seatRepository.saveAndFlush(seat);
        }catch (ObjectOptimisticLockingFailureException ex) {

        }
        Booking booking = new Booking();
        booking.setSeat(seat);
        booking.setUser(user);
        booking.setBookingTime(LocalDateTime.now());
        return bookingRepository.save(booking);
    }
    public List<BookingResponseDto> getMyBookings(String username) {
        return bookingRepository.findByUserUsername(username)
                .stream()
                .map(booking -> new BookingResponseDto(
                        booking.getId(),
                        booking.getUser().getUsername(),
                        booking.getSeat().getSeatNumber(),
                        booking.getBookingTime(),
                        booking.getStatus().name()
        )).toList();
    }
@Transactional
    public void cancelBooking(Long bookingId, String username, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found" + bookingId));

        if(!isAdmin && !booking.getUser().getUsername().equals(username)) {
            throw new ForbiddenOperationException("You can cancel only your booking");
        }
        if(booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new BookingConflictException("Booking is already cancelled");
        }
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Seat seat = booking.getSeat();
        seat.setBooked(false);
        seatRepository.save(seat);
        bookingRepository.save(booking);
    }
    public AdminDashboardResponse getDashboardStats() {
        long totalSeats = seatRepository.count();
        long availableSeats = seatRepository.countByBookedFalse();
        long confirmedSeats = bookingRepository.countByStatus(Booking.BookingStatus.CONFIRMED);
        long totalUsers = userRepository.count();
        return new AdminDashboardResponse(totalSeats, availableSeats, confirmedSeats, totalUsers);
    }
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll().stream().map(booking -> new BookingResponseDto(
                booking.getId(),
                booking.getUser().getUsername(),
                booking.getSeat().getSeatNumber(),
                booking.getBookingTime(),
                booking.getStatus().name()
        )).toList();
    }

}
