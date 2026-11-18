package com.anish.seatbooking;

import com.anish.seatbooking.entity.Booking;
import com.anish.seatbooking.entity.Seat;
import com.anish.seatbooking.repository.SeatRepository;
import com.anish.seatbooking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingConcurrentTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SeatRepository seatRepository;
    @Test
    void testConcurrentBookingTest() throws Exception {
        Seat seat = new Seat("CONCURRENT-" + java.util.UUID.randomUUID());
        seat.setBooked(false);
        Seat savedSeat = seatRepository.saveAndFlush(seat);
        Long seatId = savedSeat.getId();
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Callable<Booking> taskA = () -> {
                latch.await();
                return bookingService.seatBook(seatId, "userA");
            };
            Callable<Booking> taskB = () -> {
                latch.await();
                return bookingService.seatBook(seatId, "userB");
            };

            Future<Booking> futureA = service.submit(taskA);
            Future<Booking> futureB = service.submit(taskB);
            latch.countDown();
            int successCount = 0;
            int failedCount = 0;
            try {
                futureA.get();
                successCount++;
            } catch (ExecutionException ex) {
                failedCount++;
                System.out.println("UserA failed" + ex.getCause());
            }
            try {
                futureB.get();
                successCount++;
            } catch (ExecutionException ex) {
                failedCount++;
                System.out.println("UserB failed" + ex.getCause());
            }
            service.shutdown();
            System.out.println("Successfull bookings: " + successCount);
            System.out.println("Failed bookings: " + failedCount);
            assertEquals(1, successCount);
            assertEquals(1, failedCount);
        } finally {
            service.shutdown();
        }
    }
}
