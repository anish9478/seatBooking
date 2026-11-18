package com.anish.seatbooking.service;

import com.anish.seatbooking.entity.Seat;
import com.anish.seatbooking.exception.ResourceNotFoundException;
import com.anish.seatbooking.exception.SeatAlreadyBookedException;
import com.anish.seatbooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }
    public Seat createSeat(Seat seat) {
        if(seatRepository.existsBySeatNumber(seat.getSeatNumber())) {
            throw new SeatAlreadyBookedException("Seat number already exists");
        }
        return seatRepository.save(seat);
    }

    public Seat getById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("seat not found :" +id));
    }


}
