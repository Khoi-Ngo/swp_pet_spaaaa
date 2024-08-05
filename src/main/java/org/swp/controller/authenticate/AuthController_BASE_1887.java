package org.swp.controller.authenticate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.dto.request.RefreshRequest;
import org.swp.dto.request.SignInRequest;
import org.swp.dto.request.SignUpRequest;
import org.swp.dto.response.JwtAuthenticationResponse;
import org.swp.entity.User;
import org.swp.service.AuthenticationService;

import java.util.Objects;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    //ENDPOINTS
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            User newUser = authenticationService.signUp(signUpRequest);
            return Objects.isNull(newUser) ?
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Maybe the username or email is in use already")
                    : ResponseEntity.ok(newUser)
                    ;
        } catch (Exception e) {
            logger.error("Error occurred during sign up: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        try {
            return ResponseEntity.ok(authenticationService.signIn(signInRequest));
        } catch (Exception e) {
            logger.error("Error occurred during sign in: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(authenticationService.refresh(token));
        } catch (Exception e) {
            logger.error("Error occurred during refresh: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
