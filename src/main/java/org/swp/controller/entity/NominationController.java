package org.swp.controller.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.NominationRequest;
import org.swp.service.NominationService;

@RestController
@RequestMapping("/api/v1/nomination")
public class NominationController {

    private static final Logger logger = LoggerFactory.getLogger(NominationController.class);

    @Autowired
    private NominationService NominationService;

    //Create nomination
    @PostMapping("/create")
    public ResponseEntity<?> createNomination(@RequestHeader("Authorization") String token,
                                              @RequestBody NominationRequest nominationRequest) {
        try {
            boolean isCreated = NominationService.createNomination(token, nominationRequest);
            if (!isCreated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nomination not created");
            }
            return ResponseEntity.ok("Nomination created successfully");
        } catch (Exception e) {
            logger.error("Error while creating nomination", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Get Nominations of Serivce
    @GetMapping("service/{serviceId}")
    public ResponseEntity<?> getNominationOfService(@PathVariable int serviceId) {
        try {
            var nominations = NominationService.getAllNominationOfService(serviceId);
            if (nominations == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nomination not found");
            }
            return ResponseEntity.ok(nominations);
        } catch (Exception e) {
            logger.error("Error while getting all nominations of service", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Get Nomination of Shop
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<?> getNominationOfShop(@PathVariable int shopId) {
        try {
            var nominations = NominationService.getAllNominationOfShop(shopId);
            if (nominations == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nomination not found");
            }
            return ResponseEntity.ok(nominations);
        } catch (Exception e) {
            logger.error("Error while getting all nominations of shop", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }

}
