package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.configuration.constant.shop.ShopConstantNumber;
import org.swp.dto.request.CreateShopRequest;
import org.swp.entity.Shop;
import org.swp.service.ShopService;

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

    //LATEST SHOPS

    @GetMapping("/all")
    public ResponseEntity<?> getAllShops() {
        try{
            var shops = shopService.getAllShops();
            if(shops == null){
                return ResponseEntity.status(404).body("Shops not found");
            }
            return ResponseEntity.ok(shops);
        } catch (Exception e){
            logger.error("Error while getting all shops", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    //get shop detail
    @GetMapping("/{id}")
    public ResponseEntity<?> getShopDetail(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(shopService.getShopDetail(id));
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
            return Objects.nonNull(request) ?
                    ResponseEntity.ok(shopService.createShop(request)) :
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid information");
        }
        catch (Exception e) {
            logger.error("Error while creating shop", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //update shop

    //delete shop
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(shopService.deleteShop(id));
        } catch (Exception e) {
            logger.error("Cannot delete the shop" + e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot delete the shop");
        }
    }


}