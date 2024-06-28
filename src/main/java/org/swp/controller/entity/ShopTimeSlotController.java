package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.CreateShopTimeSlotRequest;
import org.swp.dto.request.UpdateShopTimeSlotRequest;
import org.swp.service.ShopTimeSlotService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/shop-timeslot")
public class ShopTimeSlotController {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(ShopTimeSlotController.class);

    @Autowired
    private ShopTimeSlotService shopTimeSlotService;
    /*
     * SHOP timeslot
     *
     */
    //create

    @PostMapping
    public ResponseEntity<?> createShopTimeSlot(@RequestBody CreateShopTimeSlotRequest request) {
        try {
            return Objects.nonNull(request) ?
                    ResponseEntity.ok(shopTimeSlotService.createShopTimeSlot(request))
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid information");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot create time slot");
        }
    }



//
//    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShopTimeSlot(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(shopTimeSlotService.deleteShopTimeSlot(id));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot delete shop time slot");
        }
    }
//




    //update
    @PatchMapping
    public ResponseEntity<?> updateShopTimeSlot(@RequestBody UpdateShopTimeSlotRequest request) {
        try {
            return Objects.nonNull(request) ?
                    ResponseEntity.ok(shopTimeSlotService.updateShopTimeSlot(request))
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid information");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot update time slot");
        }
    }


//
//    //get all (note no need to get any detail)
    @GetMapping("/all/auth")
    public ResponseEntity<?> getAllShopTimeSlots(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(shopTimeSlotService.getAllShopTimeSlot(token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot get all shop time slot");
        }
    }



}
