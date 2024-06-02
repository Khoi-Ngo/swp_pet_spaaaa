package org.swp.controller.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swp.configuration.constant.shop.ShopConstantNumber;
import org.swp.enums.TypePet;
import org.swp.service.ShopService;

@RestController
@RequestMapping("/api/v1")
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

    @GetMapping("/most-rcmd-shops-dog")
    public ResponseEntity<?> getMostRcmdShopsDog() {
        return ResponseEntity.ok(shopService
                .getMostRcmdShops(TypePet.DOG
                        , ShopConstantNumber.NUMBER_OF_MOST_RCMD_SHOP.getValue()));
    }

    @GetMapping("/most-rcmd-shops-cat")
    public ResponseEntity<?> getMostRcmdShopsCat() {
        return ResponseEntity.ok(shopService
                .getMostRcmdShops(TypePet.CAT
                        , ShopConstantNumber.NUMBER_OF_MOST_RCMD_SHOP.getValue()));
    }
}