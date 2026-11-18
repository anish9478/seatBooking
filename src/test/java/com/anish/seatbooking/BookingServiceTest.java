package com.anish.seatbooking;

import com.anish.seatbooking.dtos.BookingResponseDto;
import com.anish.seatbooking.entity.Booking;
import com.anish.seatbooking.entity.Seat;
import com.anish.seatbooking.entity.User;
import com.anish.seatbooking.exception.BookingConflictException;
import com.anish.seatbooking.exception.ForbiddenOperationException;
import com.anish.seatbooking.exception.SeatAlreadyBookedException;
import com.anish.seatbooking.repository.SeatRepository;
import com.anish.seatbooking.repository.UserRepository;
import com.anish.seatbooking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldBookAvailableSeat() {
//        create a user
        User user = new User();
        user.setUsername("testUser_" + java.util.UUID.randomUUID());
        user.setPassword("test123");
        User savedUser = userRepository.saveAndFlush(user);
//        create an available seat
        Seat seat = new Seat("TEST-" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);
//        do booking
        Booking booking = bookingService.seatBook(savedSeat.getId(), savedUser.getUsername());
//        verify booking
        assertNotNull(booking.getId());
        assertEquals(savedUser.getUsername(), booking.getUser().getUsername());
//        verify seat is booked
        Seat updateSeat = seatRepository.findById(savedSeat.getId()).orElseThrow();
        assertTrue(updateSeat.isBooked());
    }

    @Test
    void shouldNotBookAlreadyBookedSeat() {
        User user = new User();
        user.setUsername("testUser_" + java.util.UUID.randomUUID());
        user.setPassword("test123");
        User savedUser = userRepository.saveAndFlush(user);
        Seat seat = new Seat("BOOKED-" + java.util.UUID.randomUUID());
        seat.setBooked(true);
        Seat savedSeat = seatRepository.saveAndFlush(seat);
        SeatAlreadyBookedException ex = assertThrows(SeatAlreadyBookedException.class,
                () -> bookingService.seatBook(savedSeat.getId(), savedUser.getUsername()));
        assertEquals("Seat is already booked", ex.getMessage());

    }

    @Test
    void shouldCancelBookedTicket() {
        User user = new User();
        user.setUsername("TestUser123-" + java.util.UUID.randomUUID());
        user.setPassword("test123");
        User savedUser = userRepository.saveAndFlush(user);

        Seat seat = new Seat("CANCEL-" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);

        Booking booking = bookingService.seatBook(savedSeat.getId(), savedUser.getUsername());

        bookingService.cancelBooking(booking.getId(), savedUser.getUsername(), false);

        BookingResponseDto cancelledBooking = bookingService.getMyBookings(savedUser.getUsername()).stream()
                .filter(b -> b.getBookingId().equals(booking.getId()))
                .findFirst().orElseThrow();
        assertEquals("CANCELLED", cancelledBooking.getStatus());

        Seat updateSeat = seatRepository.findById(savedSeat.getId()).orElseThrow();
        assertFalse(updateSeat.isBooked());
    }

    @Test
    void shouldNotAllowOtherUserToCancelBooking() {
        User owner = new User();
        owner.setUsername("OWNER-" + java.util.UUID.randomUUID());
        owner.setPassword("test123");
        owner = userRepository.saveAndFlush(owner);

        User otherUser = new User();
        otherUser.setUsername("OWNER-" + java.util.UUID.randomUUID());
        otherUser.setPassword("test123");
        otherUser = userRepository.saveAndFlush(otherUser);

        Seat seat = new Seat("OWNER-" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);

        Booking booking = bookingService.seatBook(savedSeat.getId(), owner.getUsername());
        User finalOtherUser = otherUser;
        assertThrows(ForbiddenOperationException.class, () -> bookingService.cancelBooking(
                booking.getId(), finalOtherUser.getUsername(), false
        ));

        BookingResponseDto dto = bookingService.getMyBookings(owner.getUsername()).get(0);
        assertEquals("CONFIRMED", dto.getStatus());

    }

    @Test
    void shouldNotCancelAlreadyCancelledBooking() {
        User user = new User();
        user.setUsername("CANCEL_" + java.util.UUID.randomUUID());
        user.setPassword("test123");
        User savedUser = userRepository.saveAndFlush(user);

        Seat seat = new Seat("DOOUBLE-CANCEL-" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);

        Booking booking = bookingService.seatBook(savedSeat.getId(), savedUser.getUsername());
        bookingService.cancelBooking(booking.getId(), savedUser.getUsername(), false);
        assertThrows(BookingConflictException.class, () -> bookingService.cancelBooking(booking.getId(),
                savedUser.getUsername(), false));
    }
}
