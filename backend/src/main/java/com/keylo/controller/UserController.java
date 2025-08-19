package com.keylo.controller;

import com.keylo.dto.LoginRequest;
import com.keylo.dto.JwtResponse;
import com.keylo.model.User;
import com.keylo.service.UserService;
import com.keylo.security.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    //! register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.emailExists(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User savedUser = userService.registerUser(user); // use encrypted password
        return ResponseEntity.ok(savedUser);
    }

    //! login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
            System.out.println("Login attempt: email=" + loginRequest.getEmail() + ", password=" + loginRequest.getPassword());
        try {
            JwtResponse jwtResponse = userService.loginUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }

    //! refresh token
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
            }

            String oldToken = authHeader.substring(7);
            String email = jwtUtils.extractEmail(oldToken); // This might throw ExpiredJwtException
            
            // Generate new token
            String newToken = jwtUtils.refreshToken(email);
            User user = userService.loadUserByEmail(email);
            
            JwtResponse jwtResponse = new JwtResponse(newToken, user.getId(), user.getEmail());
            return ResponseEntity.ok(jwtResponse);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to refresh token. Please login again.");
        }
    }

    //!user actions  
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user");
        }
    }

    //! logout - invalidate token on client side
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Since we're using stateless JWT, we just return success
        // The client should remove the token from storage
        return ResponseEntity.ok().body("Logged out successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user");
        }
    }
}