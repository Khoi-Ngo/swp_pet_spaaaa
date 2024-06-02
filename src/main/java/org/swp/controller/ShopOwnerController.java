package org.swp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.RequestAcceptBooking;
import org.swp.service.BookingService;
import org.swp.util.SecurityUtil;

import java.util.Objects;

@RestController
@RequestMapping("api/v1/shop-owner")
public class ShopOwnerController {
    private static final Logger logger = LoggerFactory.getLogger(ShopOwnerController.class);

    @Autowired
    private BookingService bookingService;

    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
//        String userName = SecurityUtil.getUserName(SecurityContextHolder.getContext());
        String userName = null;
        return Objects.nonNull(userName) ?
                ResponseEntity.ok(bookingService.getAllBookings(userName))
                : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable("id") int id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PostMapping("/booking/accept")
    public ResponseEntity<?> acceptAction(@RequestBody RequestAcceptBooking request) {
        return ResponseEntity.ok(bookingService.accept(request));
    }

}
