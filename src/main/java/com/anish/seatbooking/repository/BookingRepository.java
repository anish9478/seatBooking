package com.anish.seatbooking.repository;

import com.anish.seatbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_id(Long userId);
    boolean existsBySeat_id(Long seatId);
    Optional<Booking> findBySeatId(Long seatId);
    List<Booking> findByUserUsername(String username);
    long countByStatus(Booking.BookingStatus status);
}
