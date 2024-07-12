package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swp.service.CacheShopTimeSlotService;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/cache-shop-time-slot/")
public class CacheShopTimeSlotController {
    private static final Logger logger = LoggerFactory.getLogger(CacheShopTimeSlotController.class);

    @Autowired
    private CacheShopTimeSlotService cacheShopTimeSlotService;

    @GetMapping("/{serviceId}/{date}")
    public ResponseEntity<?> getAvailableTimeSlotsForDate(@PathVariable("serviceId") int id
            , @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            return ResponseEntity.ok(cacheShopTimeSlotService.getSlotInfors(id, date));
        } catch (Exception e) {
            logger.error("Error while getting information slots for a date in Service Detail page");
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found date or not found the service");
        }
    }
    //no need CRUD here
}
