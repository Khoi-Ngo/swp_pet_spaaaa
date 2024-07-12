package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.RequestBookingRequest;
import org.swp.dto.request.RequestCancelBookingRequest;
import org.swp.dto.response.MarkCompleteBookingRequest;
import org.swp.enums.BookingStatus;
import org.swp.service.BookingService;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/booking/auth")
public class BookingController {


    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;


    @GetMapping
    public ResponseEntity<?> getAllBookings(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(bookingService.getAllBookings(token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find data of booking");
        }
    }


    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody RequestBookingRequest request) {
        try {
            return ResponseEntity.ok(bookingService.createBooking(request));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating booking");
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@RequestBody RequestCancelBookingRequest request,
                                           @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(bookingService.cancel(request, token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while cancelling booking");
        }
    }

    //MARK COMPLETED
    @PostMapping("/complete")
    public ResponseEntity<?> markCompleted(@RequestBody MarkCompleteBookingRequest request,
                                           @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(bookingService.markBooking(request.getBookingId(), BookingStatus.COMPLETED, token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while marking booking");
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(bookingService.getBookingById(id));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/cache/{cacheShopTimeSlotId}")
    public ResponseEntity<?> getAllBookingByTimeSlot(@PathVariable("cacheShopTimeSlotId") int cacheShopTimeSlotId,
                                                     @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(bookingService.getAllBookings(cacheShopTimeSlotId, token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
