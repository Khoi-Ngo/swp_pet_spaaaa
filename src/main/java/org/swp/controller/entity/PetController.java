package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.CreatePetRequest;
import org.swp.dto.request.UpdatePetRequest;
import org.swp.enums.TypePet;
import org.swp.service.PetService;

import java.util.Objects;

@RestController
@RequestMapping("api/v1/pet")//todo pet -> already login (customer, admin role)
public class PetController {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }


//    @GetMapping("/pet-types") // test to use VSCode
//    public ResponseEntity<?> getAllPetTypesIntSystem() {
//        try {
//            var petTypes = TypePet.values();
//            return ResponseEntity.ok(petTypes);
//
//        } catch (Exception e) {
//            logger.error("There was an error while getting pet types in system");
//            logger.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot find all pet types");
//        }
//    }

    @GetMapping("/all/auth")
    public ResponseEntity<?> getAllPets(@RequestHeader(name = "Authorization") String token) {
        try {
            return Objects.nonNull(token) ?
                    ResponseEntity.ok(petService.getAllPets(token)) :
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the pet");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetDetail(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(petService.getPetDetail(id));
        } catch (Exception e) {
            logger.error("Cannot find the pet" + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the pet");
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable("id") int id,
                                       @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(petService.deletePet(id, token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot delete the pet");
        }
    }

    //UPDATE
    @PutMapping
    public ResponseEntity<?> updatePet(@RequestBody UpdatePetRequest request) {
        try {
            return ResponseEntity.ok(petService.updatePet(request));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot update the pet");
        }
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> createPet(@RequestBody CreatePetRequest request) {
        try {
            return ResponseEntity.ok(petService.createPet(request));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot create the pet");
        }
    }
}
