package org.swp.controller.entity.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.service.AdminService;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("manageShopOwner/viewAll")
    public ResponseEntity<?> getAllAccShopOwner(){
        try {
            var accountsShopOwner = adminService.getAllShopOwner();
            if (accountsShopOwner == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("account shopOwner not found");
            }
            return ResponseEntity.ok(accountsShopOwner);
        } catch (Exception e) {
            logger.error("Error while getting all account shopOwner", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/manageCustomer/viewAll")
    public ResponseEntity<?> getAllCustomer() {
        try {
            var accountCustomer = adminService.getAllCustomer();
            if (accountCustomer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            }
            return ResponseEntity.ok(accountCustomer);
        } catch (Exception e) {
            logger.error("Error while getting all account Customer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
