package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.NomiCreateRequest;
import org.swp.service.NominationService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/nomination")
public class NominationController {
    @Autowired
    private NominationService nominationService;
    private static final Logger logger = LoggerFactory.getLogger(NominationController.class);

    //customer create nomination
    @PostMapping
    public ResponseEntity<?> createNomination(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody NomiCreateRequest request) {
        try {
            var response = nominationService.createNomination(token, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("There was an error creating nomination");
        }
    }

    //customer delete nomination
    @DeleteMapping("/{nominationId}")
    public ResponseEntity<?> deleteNomination(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable("nominationId") int nominationId) {
        try {
            return ResponseEntity.ok(nominationService.deleteNomination(token, nominationId));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("There was an error deleting nomination");
        }
    }

    @GetMapping("{shopId}")
    public ResponseEntity<?> getNominationDetail(@RequestHeader(name = "Authorization") String token,
                                                 @PathVariable("shopId") Integer shopId) {
        try {
            return ResponseEntity.ok(nominationService.getNominationByUserAndShop(token, shopId));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //get all nomination of a user
    @GetMapping
    public ResponseEntity<?> getAllNomination(@RequestHeader(name = "Authorization") String token) {
        try {
            var response = nominationService.getNominationHistory(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //get all nomination of shop
    @GetMapping("all/{shopId}")
    public ResponseEntity<?> getAllNominationOfShop(@PathVariable("shopId") int shopId) {
        try {
            var nomi = nominationService.getAllNominationOfShop(shopId);
            if (Objects.isNull(nomi)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nomination not found for shopId: " + shopId);
            }
            return ResponseEntity.ok(nomi);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
