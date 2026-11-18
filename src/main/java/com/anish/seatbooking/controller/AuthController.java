package com.anish.seatbooking.controller;

import com.anish.seatbooking.dtos.RegisterRequest;
import com.anish.seatbooking.entity.User;
import com.anish.seatbooking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterRequest request) {
        User user = authService.register(
                request.getUsername(),
                request.getPassword()
        );
        return Map.of("Id", user.getId(), "username", user.getUsername(),
                "message", "login user is successfull");
    }
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody RegisterRequest request) {
        String token = authService.login(
                request.getUsername(),
                request.getPassword()
        );
        return Map.of("token", token, "message", "login successfull");
    }
    @PostMapping("/admin")
    public Map<String, Object> adminPanel(@RequestBody RegisterRequest request) {
        User user = authService.createAdmin(request.getUsername(), request.getPassword());
        return Map.of("id", user.getId(), "username", user.getUsername(),
                "role", user.getRole(), "message", "admin created successfully");
    }


}
