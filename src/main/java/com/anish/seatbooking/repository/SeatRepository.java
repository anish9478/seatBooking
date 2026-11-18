package com.anish.seatbooking.repository;

import com.anish.seatbooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    boolean existsBySeatNumber(String seatNumber);
    long countByBookedTrue();
    long countByBookedFalse();

}
