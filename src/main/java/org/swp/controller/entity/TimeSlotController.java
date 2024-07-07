//package org.swp.controller.entity;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.swp.service.TimeSlotService;
//
//
//@RestController
//@RequestMapping("/api/v1/timeslot")
//public class TimeSlotController {
//
//    @Autowired
//    private static final Logger logger = LoggerFactory.getLogger(TimeSlotController.class);
//
//    @Autowired
//    private TimeSlotService timeSlotService;
//
//    @GetMapping("/all")
//    public ResponseEntity<?> getAllTimeSlots() {
//        try {
//            return ResponseEntity.ok(timeSlotService.getAllTimeSlot());
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot get all time slot");
//        }
//    }
//}
