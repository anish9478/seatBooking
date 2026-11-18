package com.anish.seatbooking;

import com.anish.seatbooking.entity.Booking;
import com.anish.seatbooking.entity.Seat;
import com.anish.seatbooking.repository.BookingRepository;
import com.anish.seatbooking.repository.SeatRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldBookAvailableSeat() throws Exception {
        Seat seat = new Seat("BOOK-TEST-" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);
        mockMvc.perform(post("/api/bookings/" + savedSeat.getId())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldNotBookAlreadyBookedSeat() throws Exception {
        Seat seat = new Seat("BOOK-TEST" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);

        mockMvc.perform(post("/api/bookings/" + savedSeat.getId())).andExpect(status().isOk());
        mockMvc.perform(post("/api/bookings/" + savedSeat.getId())).andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldCancelOwnBooking() throws Exception {
        Seat seat = new Seat("CANCEL-TEST-" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);

        mockMvc.perform(post("/api/bookings/" + savedSeat.getId())).andExpect(status().isOk());
        Booking booking = bookingRepository.findBySeatId(savedSeat.getId()).orElseThrow();
        mockMvc.perform(delete("/api/bookings/" + booking.getId())).andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldNotAllowOtherUserToCancelBooking() throws Exception {
        Seat seat = new Seat("FORBIDDEN-TEST-" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);
        mockMvc.perform(post("/api/bookings/" + savedSeat.getId())).andExpect(status().isOk());

        Booking booking = bookingRepository.findBySeatId(savedSeat.getId()).orElseThrow();
        mockMvc.perform(delete("/api/bookings/" + booking.getId())
                        .with(user("userB").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void shouldGetMyBookings() throws Exception {
        Seat seat = new Seat("MY-BOOKING-" + UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);
        mockMvc.perform(post("/api/bookings/" + savedSeat.getId())).andExpect(status().isOk());

        mockMvc.perform(get("/api/bookings/my")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].username", Matchers.everyItem(
                        Matchers.is("userA"))))
                .andExpect(jsonPath("$[*].seatNumber", Matchers.hasItem(savedSeat.getSeatNumber())));


    }
}
