package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.UpdatePasswordRequest;
import org.swp.dto.request.UpdateUserProfileRequest;
import org.swp.service.UserService;

import java.util.Objects;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    //no auth
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        try {
            return Objects.nonNull(username) ?
                    ResponseEntity.ok(userService.getUserByUsername(username))
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the user");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the user");
        }
    }

    //user profile (private)
    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestHeader(name = "Authorization") String token) {
        try {
            return Objects.nonNull(token) ?
                    ResponseEntity.ok(userService.getUserProfile(token))
                    : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot get the profile");
        }
    }

    //UPDATE USER PROFILE + CHANGE PASSWORD
    @PatchMapping
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateUserProfileRequest request) {
        try {
            var response = userService.updateUserProfile(request);
            return Objects.nonNull(response) ?
                    ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are some invalid stuffs");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find the user");
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String token,
                                            @RequestBody UpdatePasswordRequest request) {
        try {
                return ResponseEntity.ok(userService.updatePassword(token, request));
        } catch (Exception e) {
            logger.error("Error while updating password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/avata/auth")
    public ResponseEntity<?> getUserAvata(@RequestHeader(name = "Authorization") String token) {
        try {
            return Objects.nonNull(token) ?
                    ResponseEntity.ok(userService.getUserAvata(token))
                    : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot get the profile");
        }
    }


}
