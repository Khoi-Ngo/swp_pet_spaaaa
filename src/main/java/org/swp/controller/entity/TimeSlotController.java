package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.CreateTimeSlotRequest;
import org.swp.dto.request.UpdateTimeSlotRequest;
import org.swp.service.TimeSlotService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/timeslot")
public class TimeSlotController {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(TimeSlotController.class);

    @Autowired
    private TimeSlotService timeSlotService;
    /*
     * Timeslot
     *
     */
    //create
    @PostMapping
    public ResponseEntity<?> createTimeSlot(@RequestBody CreateTimeSlotRequest request) {
        try {
            return Objects.nonNull(request) ?
                    ResponseEntity.ok(timeSlotService.createTimeSlot(request))
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid information");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot create time slot");
        }
    }


//    //delete
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTimeSlot(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(timeSlotService.deleteTimeSlot(id));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot create time slot");
        }
    }

    //update
    @PatchMapping
    public ResponseEntity<?> updateTimeSlot(@RequestBody UpdateTimeSlotRequest request) {
        try {
            return Objects.nonNull(request) ?
                    ResponseEntity.ok(timeSlotService.updateTimeSlot(request))
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid information");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot update time slot");
        }
    }

    //get all (note no need to get any detail)
    @GetMapping("/all")
    public ResponseEntity<?> getAllTimeSlots() {
        try {
            return ResponseEntity.ok(timeSlotService.getAllTimeSlot());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot get all time slot");
        }
    }
}
