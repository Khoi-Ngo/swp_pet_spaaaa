package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.NomiCreateRequest;
import org.swp.dto.request.NominationDeleteRequest;
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
    @DeleteMapping
    public ResponseEntity<?> createNomination(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody NominationDeleteRequest request) {
        try {
            var response = nominationService.deleteNomination(token, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().body("There was an error deleting nomination");
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

}
