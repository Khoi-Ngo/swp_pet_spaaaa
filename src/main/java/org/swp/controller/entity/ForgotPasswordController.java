package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swp.dto.request.PasswordChangeRequest;
import org.swp.service.SendEmailService;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth/password")
public class ForgotPasswordController {

    @Autowired
    SendEmailService sendEmailService;

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPasswordProcess(@RequestBody Map<String, String> request) {
        try {
            return Objects.nonNull(request) ?
                    ResponseEntity.ok(sendEmailService.handleForgotPassword(request))
                    : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find data of email");
        }
    }

    // check if the token is valid or not (used in be)
    @GetMapping("/checkValidate")
    public ResponseEntity<?> validateResetToken(@RequestParam("token") String token) {
        try {
            return Objects.nonNull(token) ?
                    ResponseEntity.ok(sendEmailService.checkToken(token))
                    : ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find data of email");
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            return ResponseEntity.ok(sendEmailService.changePassword(request));
        } catch (Exception e) {
            logger.error("Error while updating password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
