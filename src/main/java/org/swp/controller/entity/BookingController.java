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
            return Objects.nonNull(token) ?
                    ResponseEntity.ok(bookingService.getAllBookings(token))
                    : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find data of booking");
        }
    }


    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody RequestBookingRequest request) {
        //if create for existed pet -> petId
        //if new pet todo compare with create pet
        try {
            var response = bookingService.createBooking(request);
            return Objects.nonNull(response) ?
                    ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are some invalid stuffs");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating booking");
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@RequestBody RequestCancelBookingRequest request) {
        try {
            return ResponseEntity.ok(bookingService.cancel(request));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while cancelling booking");
        }
    }


    //MARK COMPLETED
    @PutMapping("{id}")
    public ResponseEntity<?> markCompleted(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(bookingService.markBooking(id, BookingStatus.COMPLETED));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while marking booking");
        }
    }


    //MARK CANCELED
    @DeleteMapping("{id}")
    public ResponseEntity<?> markCanceled(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(bookingService.markBooking(id, BookingStatus.COMPLETED));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while marking booking");
        }
    }


    //==========
    @GetMapping("{id}")
    public ResponseEntity<?> getBookingById(@PathVariable("id") int id) {
        Object responseData = bookingService.getBookingById(id);
        return Objects.nonNull(responseData) ? ResponseEntity.ok(responseData)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //LocalDate + Type //todo
    @GetMapping("{date}/{type}")
    public ResponseEntity<?> getBookingByDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                              @PathVariable("type") String type) {

        return null;
    }

    @GetMapping("/shop")
    public ResponseEntity<?> getAllBookingsByShop(@RequestHeader(name = "Authorization") String token) {
        try {
            return Objects.nonNull(token) ?
                    ResponseEntity.ok(bookingService.getAllBookingsByShop(token))
                    : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find data of booking");
        }
    }


}
