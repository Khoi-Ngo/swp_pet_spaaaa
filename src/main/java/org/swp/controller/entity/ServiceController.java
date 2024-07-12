package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.configuration.constant.service.ServiceConstantNumber;
import org.swp.dto.request.CreateServiceRequest;
import org.swp.dto.request.UpdateServiceRequest;
import org.swp.service.CategoryServiceService;
import org.swp.service.ServiceService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/service")
public class ServiceController { //todo -> some action should be more authenticated

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private CategoryServiceService categoryServiceService;

    @GetMapping("/latest-services")
    public ResponseEntity<?> getLatestServices() {
        try {
            return ResponseEntity.ok(serviceService.getLatestServices(ServiceConstantNumber.NUMBER_OF_LATEST_SERVICES.getValue()));
        } catch (Exception e) {
            logger.error("Error while getting latest services", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/most-rcmd-services")
    public ResponseEntity<?> getMostRcmdServices() {
        try {
            return ResponseEntity.ok(serviceService.getMostRcmdServices(ServiceConstantNumber.NUMBER_OF_MOST_RCMD_SERVICES.getValue()));
        } catch (Exception e) {
            logger.error("Error while getting most recommended services", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/category-services")
    public ResponseEntity<?> getAllCategoryServices() {
        try {
            return ResponseEntity.ok(categoryServiceService.getAll());
        } catch (Exception e) {
            logger.error("Error while getting all category services", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllServices() {
        try {
            return ResponseEntity.ok(serviceService.getAll());
        } catch (Exception e) {
            logger.error("Error while getting all services", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("{id}")//done
    public ResponseEntity<?> getServiceById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(serviceService.getServiceById(id));
        } catch (Exception e) {
            logger.error("Error while getting service by id", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all/{shopId}")
    public ResponseEntity<?> getServiceByShopId(@PathVariable("shopId") int shopId) {
        //todo
        try {
            return ResponseEntity.ok(serviceService.getAllServiceByShopId(shopId));
        } catch (Exception e) {
            logger.error("Error while getting service by shopId", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //UPDATE
    @PutMapping//done
    public ResponseEntity<?> updateService(@RequestBody UpdateServiceRequest request) {
        try {
            return ResponseEntity.ok(serviceService.updateService(request));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while update service");
        }
    }

    //CREATE
    @PostMapping//done
    public ResponseEntity<?> createService(@RequestBody CreateServiceRequest request) {
        try {
            return ResponseEntity.ok(serviceService.createService(request));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating service");
        }
    }

    //DELETE
    @DeleteMapping("/{id}")//done
    public ResponseEntity<?> deleteService(@PathVariable("id") int id,
                                           @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(serviceService.deleteService(id, token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting service");
        }
    }

    @GetMapping("/all/auth")//done
    public ResponseEntity<?> getAllServicesOfShopowner(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(serviceService.getAllOfShopowner(token));
        } catch (Exception e) {
            logger.error("Error while getting all services", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
