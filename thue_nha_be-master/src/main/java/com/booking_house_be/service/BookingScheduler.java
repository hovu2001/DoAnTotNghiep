package com.booking_house_be.service;

import com.booking_house_be.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class BookingScheduler   {
    @Autowired
    private IBookingService bookingService;

    @Scheduled(fixedRate = 60000) // Chạy mỗi phút
    @Transactional
    public void autoCancelPendingBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> pendingBookings = bookingService.findByStatus("Chờ xác nhận");

        for (Booking booking : pendingBookings) {
            LocalDateTime createAt = booking.getCreate_at();
            long minutesElapsed = java.time.Duration.between(createAt, now).toMinutes();

            if (minutesElapsed > 10) {
                booking.setStatus("Đã hủy");
                bookingService.save(booking);
            }
        }
    }
}
