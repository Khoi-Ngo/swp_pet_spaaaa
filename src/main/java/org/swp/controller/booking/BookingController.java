package org.swp.controller.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.RequestBookingRequest;
import org.swp.dto.request.RequestCancelBookingRequest;
import org.swp.service.BookingService;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/customer/booking")
public class BookingController {


    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;


    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        //get all bookings for a specified customer
//        String userName = SecurityUtil.getUserName(SecurityContextHolder.getContext());
        String userName = null;
        return Objects.nonNull(userName) ?
                ResponseEntity.ok(bookingService.getAllBookings(userName))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody RequestBookingRequest request) {
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

    @DeleteMapping
    public ResponseEntity<?> cancelBooking(@RequestBody RequestCancelBookingRequest request) {
        return ResponseEntity.ok(bookingService.cancel(request));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingById(@PathVariable("id") int id) {
        Object responseData = bookingService.getBookingById(id);
        return Objects.nonNull(responseData) ? ResponseEntity.ok(responseData)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("service/{id}/{date}")
    public ResponseEntity<?> getServiceByDate(@PathVariable("id") int id
            , @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            var slotInfors = bookingService.getSlotInfors(id, date);
            return Objects.nonNull(slotInfors) ?
                    ResponseEntity.ok(slotInfors)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found date or not found the service");

        } catch (Exception e) {
            logger.error("Error while getting information slots for a date in Service Detail page");
            logger.error(e.getMessage());
            return null;
        }
    }
















    /*
    Customer Actions:
Send request to cancel the booking (existing)
Send request for a new booking (existing)
Send request to reschedule a booking
View booking history
View upcoming bookings
Send a query to the Shop Owner about a booking
Update personal details
View booking details
Receive notifications for booking status changes
Request extension for an ongoing booking
Send request to modify booking details (e.g., number of participants)
Check availability for new bookings
Receive reminders for upcoming bookings
Request additional services or amenities for a booking
Shop Owner Actions:
Send request to cancel the booking (existing)
Deny the booking request (existing)
Confirm a new booking request
Suggest alternative booking slots if the requested one is unavailable
Accept or decline a reschedule request
View all pending booking requests
View all confirmed bookings
Update service availability and pricing
Generate reports on booking statistics
Notify customers of booking status changes
Block off time slots for unavailability or maintenance
Set maximum capacity for bookings and enforce limits
Automatically confirm bookings based on predefined criteria
Adjust booking durations based on specific needs
Offer promotional discounts or packages for bookings
Handle double-booking conflicts
View detailed booking information (e.g., customer preferences, special requests)
Set booking rules and policies (e.g., cancellation policy, late fee)
Receive and manage customer queries related to bookings
Send booking reminders to customers
Temporarily pause accepting new bookings (e.g., during peak times)
Prioritize bookings based on customer loyalty or membership status
     */


}
