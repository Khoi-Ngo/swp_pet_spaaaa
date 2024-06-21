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

    @DeleteMapping("manageShopOwner/delete/{id}")
    public ResponseEntity<?> deleteAccShopOwner(@PathVariable("id")){
        try {
            var accountsShopOwner = adminService.getAllShopOwner();
            if (accountsShopOwner == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("delete account shopOwner fail!");
            }
            return ResponseEntity.ok(accountsShopOwner);
        } catch (Exception e) {
            logger.error("Error while delete account shopOwner", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
