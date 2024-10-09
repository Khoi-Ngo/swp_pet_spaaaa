package org.swp.schedulerjob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.swp.service.BookingService;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Component
public class BookingTrackingJob {
    @Autowired
    private BookingService bookingService;
    private static final Logger logger = Logger.getLogger(BookingTrackingJob.class.getName());
    private static final int POLLING_INTERVAL = 10000; // millis

    private Thread trackingThread;
    private final AtomicBoolean running = new AtomicBoolean(false);

    @Scheduled(fixedRate = POLLING_INTERVAL)
    public void trackStatusBooking() {
        if (trackingThread == null || !trackingThread.isAlive()) {
            trackingThread = new Thread(() -> {
                if (!running.compareAndSet(false, true)) {
                    return; // already running
                }
                try {
                    //TODO: comment for long term deployment

                    // logger.info("Tracking status booking triggered");
                    // bookingService.trackBookingStatus(LocalDateTime.now());
                } finally {
                    running.set(false);
                }
            });
            trackingThread.start();
        }
    }

    public void stopTracking() {
        if (trackingThread != null) {
            trackingThread.interrupt();
            logger.info("Tracking status booking stopped");
        }
    }

    public void startTracking() {
        if (trackingThread == null || !trackingThread.isAlive()) {
            trackStatusBooking();  // This will initialize and start the thread if it's not running
        }
    }
}
