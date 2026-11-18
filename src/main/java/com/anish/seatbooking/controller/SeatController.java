package com.anish.seatbooking.controller;

import com.anish.seatbooking.entity.Seat;
import com.anish.seatbooking.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @PostMapping
    public Seat createSeat(@Valid  @RequestBody Seat seat) {
        return seatService.createSeat(seat);
    }
    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }
    @GetMapping("/{id}")
    public Seat getById(@PathVariable Long id) {
        return seatService.getById(id);
    }

}
