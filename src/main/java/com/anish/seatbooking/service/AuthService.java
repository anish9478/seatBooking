package com.anish.seatbooking.service;

import com.anish.seatbooking.entity.User;
import com.anish.seatbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
    public String login(String username, String password) {
       User user = userRepository.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("Invalid username & password"));
       if (!passwordEncoder.matches(password, user.getPassword())) {
           throw new RuntimeException("Invalid username & password");
       }
       return jwtService.generateToken(user.getUsername());
    }
    public User createAdmin(String username, String password) {
        if(userRepository.existsByUsername(username)) {
            throw new RuntimeException("username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ADMIN");
        return userRepository.save(user);
    }
}
