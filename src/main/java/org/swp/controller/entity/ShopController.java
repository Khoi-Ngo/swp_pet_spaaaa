package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.configuration.constant.shop.ShopConstantNumber;
import org.swp.dto.request.CreateShopRequest;
import org.swp.dto.request.UpdateShopRequest;
import org.swp.service.CacheShopTimeSlotService;
import org.swp.service.ServiceService;
import org.swp.service.ShopService;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {


    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopService shopService;

    @GetMapping("/most-rcmd-shops")
    public ResponseEntity<?> getMostRcmdShops() {
        return ResponseEntity.ok(shopService
                .getMostRcmdShops(ShopConstantNumber
                        .NUMBER_OF_MOST_RCMD_SHOP.getValue()));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllShops() {
        try {
            var shops = shopService.getAllShops();
            if (shops == null) {
                return ResponseEntity.status(404).body("Shops not found");
            }
            return ResponseEntity.ok(shops);
        } catch (Exception e) {
            logger.error("Error while getting all shops", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    //get shop detail
    @GetMapping("/auth")
    public ResponseEntity<?> getShopDetail(@RequestHeader(name = "Authorization") String token) {
        try {
            return Objects.nonNull(token) ?
                    ResponseEntity.ok(shopService.getShopDetail(token)) :
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        } catch (Exception e) {
            logger.error("Cannot find the shop" + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the shop");
        }
    }

    //create shop
    @PostMapping
    public ResponseEntity<?> createShop(@RequestBody CreateShopRequest request) {
        logger.info("Creating shop with request: {}", request);
        try {
            return ResponseEntity.ok(shopService.createShop(request));
        } catch (Exception e) {
            logger.error("Error while creating shop", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //update shop
    @PatchMapping
    public ResponseEntity<?> updateShop(@RequestBody UpdateShopRequest request,
                                        @RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(shopService.updateShop(request, token));
        } catch (Exception e) {
            logger.error("Cannot update the shop", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot update the shop");
        }
    }

//    //delete shop
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteShop(@PathVariable("id") int id,
//                                        @RequestHeader(name = "Authorization") String token) {
//        try {
//            return ResponseEntity.ok(shopService.deleteShop(id, token));
//        } catch (Exception e) {
//            logger.error("Cannot delete the shop" + e);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot delete the shop");
//        }
//    }

    //get shop detail by id
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getShopDetailById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(shopService.getShopDetailById(id));
        } catch (Exception e) {
            logger.error("Cannot find the shop" + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the shop");
        }
    }

    @GetMapping("/shopId")
    public ResponseEntity<?> getShopIdFromToken(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(shopService.getShopId(token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/dashboard/auth")
    public ResponseEntity<?> getDashboard(@RequestHeader(name = "Authorization") String token) {
        try {
            return Objects.nonNull(token) ?
                    ResponseEntity.ok(shopService.getDashboardOfShop(token)) :
                    ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        } catch (Exception e) {
            logger.error("Cannot find the shop" + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the shop");
        }
    }


    //shop owner checking infor of each timeslot per date
    @GetMapping("{date}")
    public ResponseEntity<?> getAllTimSlotInfoPerDate(@RequestHeader(name = "Authorization") String token,
                                                      @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            return ResponseEntity.ok(shopService.getAllInfoTimeSlotByDate(token, date));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


}