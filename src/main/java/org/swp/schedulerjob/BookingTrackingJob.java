package org.swp.schedulerjob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.swp.service.BookingService;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@Component
public class BookingTrackingJob {
    @Autowired
    private BookingService bookingService;
    private static final Logger logger = Logger.getLogger(BookingTrackingJob.class.getName());
    private static final int POLLING_INTERVAL = 1000;//millis

    @Scheduled(fixedRate = POLLING_INTERVAL)
    public void trackStatusBooking() {
        logger.info("Tracking status booking triggered");
        bookingService.trackBookingStatus(LocalDateTime.now());
    }
}
