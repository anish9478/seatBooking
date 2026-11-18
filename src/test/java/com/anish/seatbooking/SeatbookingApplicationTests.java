package com.anish.seatbooking;

import com.anish.seatbooking.entity.Seat;
import com.anish.seatbooking.repository.SeatRepository;
import com.anish.seatbooking.service.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SeatbookingApplicationTests {

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SeatService seatService;
    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateSeat() {
        Seat seat = new Seat("TEST-" + java.util.UUID.randomUUID());
        Seat savedSeat = seatService.createSeat(seat);
        assertNotNull(savedSeat.getId());
        assertEquals(seat.getSeatNumber(), savedSeat.getSeatNumber());

    }

    @Test
    void shouldGetSeatById() {
        Seat seat = new Seat("GET-" + java.util.UUID.randomUUID());
        Seat savedSeat = seatRepository.saveAndFlush(seat);
        Seat result = seatService.getById(savedSeat.getId());
        assertEquals(savedSeat.getId(), result.getId());
    }

    @Test
    void shouldGetAllSeats() {
        Seat seat = new Seat("ALL-" + java.util.UUID.randomUUID());
        seatRepository.saveAndFlush(seat);
        assertFalse(seatService.getAllSeats().isEmpty());
    }


}

