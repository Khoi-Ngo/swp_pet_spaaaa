package org.swp.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swp.dto.response.UserDto;
import org.swp.service.UserService;
import org.swp.entity.User;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    @Autowired
    private UserService userService;

    @GetMapping("/customer/profile/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}
