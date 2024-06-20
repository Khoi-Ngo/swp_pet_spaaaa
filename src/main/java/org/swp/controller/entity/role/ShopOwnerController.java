package org.swp.controller.entity.role;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.CreateServiceRequest;
import org.swp.dto.request.DeleteServiceRequest;
import org.swp.service.ServiceService;

import java.util.Objects;

@RestController
@RequestMapping("api/v1/shop-owner")
public class ShopOwnerController {

    @Autowired
    ServiceService serviceService;

    private static final Logger logger = LoggerFactory.getLogger(ShopOwnerController.class);

    @PostMapping("/manageService/create")
    public ResponseEntity<?> createService(@RequestBody CreateServiceRequest request){
        try{
            var response = serviceService.createService(request);
            return Objects.nonNull(response) ?
                    ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are some invalid stuffs");
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating service");
        }
    }

    @DeleteMapping("/manageService/delete")
    public ResponseEntity<?> deleteService(@RequestBody DeleteServiceRequest request){
        try{
            var response = serviceService.deleteService(request);
            return Objects.nonNull(response) ?
                    ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are some invalid stuffs");
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleteing service");
        }
    }

}
