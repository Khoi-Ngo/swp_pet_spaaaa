package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.configuration.constant.service.ServiceConstantNumber;
import org.swp.dto.request.CreateServiceRequest;
import org.swp.dto.request.DeleteServiceRequest;
import org.swp.dto.request.UpdateServiceRequest;
import org.swp.entity.Service;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.enums.TypePet;
import org.swp.repository.IServiceRepository;
import org.swp.repository.IShopRepository;
import org.swp.service.CategoryServiceService;
import org.swp.service.ServiceService;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/service")
public class ServiceController { //todo -> some action should be more authenticated

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private CategoryServiceService categoryServiceService;

    @Autowired
    private IServiceRepository serviceRepository;

    @Autowired
    private IShopRepository shopRepository;

    @GetMapping("/latest-services")
    public ResponseEntity<?> getLatestServices() {
        try {
            var services = serviceService.getLatestServices(ServiceConstantNumber.NUMBER_OF_LATEST_SERVICES.getValue());
            if (services == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Latest services not found");
            }
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            logger.error("Error while getting latest services", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/most-rcmd-services")
    public ResponseEntity<?> getMostRcmdServices() {
        try {
            var services = serviceService.getMostRcmdServices(ServiceConstantNumber.NUMBER_OF_MOST_RCMD_SERVICES.getValue());
            if (services == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Most recommended services not found");
            }
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            logger.error("Error while getting most recommended services", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/category-services")
    public ResponseEntity<?> getAllCategoryServices() {
        try {
            var categories = categoryServiceService.getAll();
            if (categories == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category services not found");
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Error while getting all category services", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllServices() {
        try {
            var services = serviceService.getAll();
            if (services == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Services not found");
            }
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            logger.error("Error while getting all services", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getServiceById(@PathVariable("id") int id) {
        try {
            var service = serviceService.getServiceById(id);
            if (service == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found with id: " + id);
            }
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            logger.error("Error while getting service by id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/all/{shopId}")
    public ResponseEntity<?> getServiceByShopId(@PathVariable("shopId") int shopId) {
        //todo
        try {
            var services = serviceService.getAllServiceByShopId(shopId);
            if (services == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Services not found for shopId: " + shopId);
            }
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            logger.error("Error while getting service by shopId", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //UPDATE
    @PutMapping
    public ResponseEntity<?> updateService(@RequestBody UpdateServiceRequest request){
        try {
            Service service = serviceRepository.findById(request.getId()).get();
            int shopId = service.getShop().getId();
            Shop shop = shopRepository.findById(shopId).get();
            if(request.getUserId() != shop.getUser().getId())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong service id");
            var response = serviceService.updateService(request);
            return Objects.nonNull(response) ?
                    ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are some invalid stuffs");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while update service");
        }
    }

    //CREATE
    @PostMapping
    public ResponseEntity<?> createService(@RequestBody CreateServiceRequest request) {
        try {
            var response = serviceService.createService(request);
            return Objects.nonNull(response) ?
                    ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are some invalid stuffs");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating service");
        }
    }

    //DELETE
    @DeleteMapping
    public ResponseEntity<?> deleteService(@RequestBody DeleteServiceRequest request) {
        try {
            var response = serviceService.deleteService(request);
            return Objects.nonNull(response) ?
                    ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are some invalid stuffs");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleteing service");
        }
    }

    @GetMapping("/all/auth")
    public ResponseEntity<?> getAllServicesOfShopowner(@RequestHeader(name = "Authorization") String token) {
        try {
            var services = serviceService.getAllOfShopowner(token);
            if (services == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Services not found");
            }
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            logger.error("Error while getting all services", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //DELETE ALL

    //DELETE SERVICE CATEGORY

    //CREATE SERVICE CATEGORY (maybe no need)

    //UPDATE SERVICE CATEGORY (maybe no need too)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
