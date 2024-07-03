package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swp.service.NotificationService;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    private NotificationService notificationService;

    //get all
    @GetMapping("/all")
    public ResponseEntity<?> getAllNotification(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(notificationService.getNotificationByUser(token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
