package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.ReferPriceCreateRequest;
import org.swp.dto.request.ReferPriceUpdateRequest;
import org.swp.service.ReferPriceService;

@RestController
@RequestMapping("/api/v1/refer-price")
public class ReferPriceController {
    @Autowired
    private ReferPriceService referPriceService;
    private static final Logger logger = LoggerFactory.getLogger(ReferPriceController.class);

    //create
    @PostMapping
    public ResponseEntity<?> createReferPrice(@RequestBody ReferPriceCreateRequest request) {
        try {
            return ResponseEntity.ok(referPriceService.createReferPrice(request));
        } catch (Exception e) {
            logger.error("There are error in creating refer price", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    //update
    @PutMapping
    public ResponseEntity<?> updateReferPrice(@RequestBody ReferPriceUpdateRequest request,
                                              @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(referPriceService.updateReferPrice(request, token));
        } catch (Exception e) {
            logger.error("There are error in updating refer price", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    //delete
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteReferPrice(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(referPriceService.deleteReferPrice(id, token));
        } catch (Exception e) {
            logger.error("There are error in deleting refer price", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    //get all
    @GetMapping("/{serviceId}")
    public ResponseEntity<?> getAllReferPrice(@PathVariable("serviceId") int serviceId) {
        try {
            return ResponseEntity.ok(referPriceService.getReferPrices(serviceId));
        } catch (Exception e) {
            logger.error("There are error in getting all refer price", e);
            return ResponseEntity.notFound().build();
        }
    }
}
