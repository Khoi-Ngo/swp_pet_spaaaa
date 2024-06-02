package org.swp.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swp.configuration.constant.service.ServiceConstantNumber;
import org.swp.enums.TypePet;
import org.swp.service.CategoryServiceService;
import org.swp.service.ServiceService;

@RestController
@RequestMapping("/api/v1")
public class ServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private CategoryServiceService categoryServiceService;

    @GetMapping("/latest-services")
    public ResponseEntity<?> getLatestServices() {
        //get latest services
        return ResponseEntity.ok(serviceService.getLatestServices(ServiceConstantNumber.NUMBER_OF_LATEST_SERVICES.getValue()));
    }

    @GetMapping("/most-rcmd-services")
    public ResponseEntity<?> getMostRcmdServices() {
        //get most recommended services
        return ResponseEntity.ok(serviceService.getMostRcmdServices(ServiceConstantNumber.NUMBER_OF_LATEST_SERVICES.getValue()));
    }

    @GetMapping("/most-rcmd-dog-services")
    public ResponseEntity<?> getMostRcmdDogServices() {
        //get most recommended dog services
        return ResponseEntity.ok(serviceService.getMostRcmdServices(TypePet.DOG, ServiceConstantNumber.NUMBER_OF_MOST_RCMD_SERVICES.getValue()));
    }

    @GetMapping("/most-rcmd-cat-services")
    public ResponseEntity<?> getMostRcmdCatServices() {
        //get most recommended cat services
        return ResponseEntity.ok(
                serviceService
                        .getMostRcmdServices(
                                TypePet.CAT
                                , ServiceConstantNumber.NUMBER_OF_MOST_RCMD_SERVICES.getValue()));
    }

    @GetMapping("/category-services")
    public ResponseEntity<?> getAllCategoryServices() {
        //get all category services
        return ResponseEntity.ok(categoryServiceService.getAll());
    }

    @GetMapping("/services")
    public ResponseEntity<?> getAllServices() {
        //get all services
        return ResponseEntity.ok(serviceService.getAll());
    }

    @GetMapping("/service/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable("id") int id) {
        //get a single service
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }


}
