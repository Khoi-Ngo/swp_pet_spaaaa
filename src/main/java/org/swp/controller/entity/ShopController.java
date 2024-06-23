package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swp.configuration.constant.shop.ShopConstantNumber;
import org.swp.service.ShopService;

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
    //create shop

    //update shop

    //delete shop



}