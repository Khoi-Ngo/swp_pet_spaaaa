package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.CreateShopTimeSlotRequest;
import org.swp.service.ShopTimeSlotService;


@RestController
@RequestMapping("/api/v1/shop-timeslot/auth")
public class ShopTimeSlotController {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(ShopTimeSlotController.class);

    @Autowired
    private ShopTimeSlotService shopTimeSlotService;

    @PostMapping
    public ResponseEntity<?> createShopTimeSlot(@RequestBody CreateShopTimeSlotRequest request) {
        try {
            return ResponseEntity.ok(shopTimeSlotService.createShopTimeSlot(request));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot create time slot");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShopTimeSlot(@PathVariable("id") int id,
                                                @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(shopTimeSlotService.deleteShopTimeSlot(id, token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot delete shop time slot");
        }
    }

//    @PatchMapping
//    public ResponseEntity<?> updateShopTimeSlot(@RequestBody UpdateShopTimeSlotRequest request) {
//        try {
//            return ResponseEntity.ok(shopTimeSlotService.updateShopTimeSlot(request));
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot update time slot");
//        }
//    }

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
