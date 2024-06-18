package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.enums.TypePet;

import java.util.List;

@RestController
@RequestMapping("api/v1/pet")//todo pet -> already login (customer, admin role)
public class PetController {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(PetController.class);


    @GetMapping("/pet-types")
    public ResponseEntity<?> getAllPetTypesIntSystem() {
        try {
            var petTypes = TypePet.values();
            return ResponseEntity.ok(petTypes);

        } catch (Exception e) {
            logger.error("There was an error while getting pet types in system");
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot find all pet types");
        }
    }

//    @GetMapping("/all")
//    public ResponseEntity<?> getAllPets() {
//
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<?> getAllPets(@RequestHeader(name = "Authorization") String token) {
//        try {
//
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Cannot find all pets");
//        }
//
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getPetDetail(@PathVariable("id") int id){
//        try{
//
//        }catch (Exception e){
//            logger.error("Cannot find the pet" + e);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the pet");
//        }
//    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deletePet(@PathVariable("id") int id) {
//        try {
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot delete the pet");
//        }
//    }
//    @DeleteMapping
//    public ResponseEntity<?> deletePets(@RequestBody List<Integer> ids){
//        try {
//
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot delete pets");
//        }
//    }

}
