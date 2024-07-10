package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.service.NotificationService;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    private NotificationService notificationService;

    //get all -> may be ignored because of Application of Websocket
    @GetMapping("/all")
    public ResponseEntity<?> getAllNotification(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(notificationService.getNotificationByUser(token));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/total")
    public ResponseEntity<?> getTotalUnreadNotification(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(notificationService.getTotalUnreadNotification(token));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    //onclick single notification
    @PostMapping("/onclick/{id}")
    public ResponseEntity<?> onClickProcess(@RequestHeader(name = "Authorization") String token,
                                            @PathVariable("id") int id
    ) {
        try {
            notificationService.updateStatusNotification(token, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            // fe have to catch this exception
            return ResponseEntity.internalServerError().build();
        }
    }
}
