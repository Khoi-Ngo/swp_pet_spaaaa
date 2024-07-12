package org.swp.controller.entity.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.SignUpRequest;
import org.swp.entity.User;
import org.swp.service.AdminService;

import java.util.Objects;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/manageShopOwner/viewAll")
    public ResponseEntity<?> getAllAccShopOwner() {
        try {
            return ResponseEntity.ok(adminService.getAllShopOwner());
        } catch (Exception e) {
            logger.error("Error while getting all account shopOwner", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("account shopOwner not found");
        }
    }

    @GetMapping("/manageCustomer/viewAll")
    public ResponseEntity<?> getAllCustomer() {
        try {
            return ResponseEntity.ok(adminService.getAllCustomer());
        } catch (Exception e) {
            logger.error("Error while getting all account Customer", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccountById(@PathVariable("id") int id,
                                               @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(adminService.deleteUserById(id, token));
        } catch (Exception e) {
            logger.error("Error while getting delete shopOwner", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/manageShopOwner/addShopOwner")
    public ResponseEntity<?> addShopOwner(@RequestBody SignUpRequest signUpRequest,
                                          @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(adminService.addShopOwner(signUpRequest, token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/viewDetail/{id}")
    public ResponseEntity<?> viewDetailAccById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(adminService.viewAccById(id));
        } catch (Exception e) {
            logger.error("Error while getting detail account", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        try {
            return ResponseEntity.ok(adminService.getDashboardOfAdmin());
        } catch (Exception e) {
            logger.error("Cannot find the shop" + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the shop");
        }
    }

    @GetMapping("/total/customer")
    public ResponseEntity<?> getTotalAccountCustomer() {
        try {
            return ResponseEntity.ok(adminService.getTotalAccountCustomer());
        } catch (Exception e) {
            logger.error("Cannot find the shop" + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the shop");
        }
    }

    @GetMapping("/total/shopOwner")
    public ResponseEntity<?> getTotalAccountShopOwner() {
        try {
            return ResponseEntity.ok(adminService.getTotalAccountShopOwner());
        } catch (Exception e) {
            logger.error("Cannot find the shop" + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the shop");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
