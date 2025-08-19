package com.keylo.controller;

import com.keylo.dto.CredentialRequest;
import com.keylo.dto.CredentialResponse;
import com.keylo.model.User;
import com.keylo.service.CredentialService;
import com.keylo.service.UserService;
import com.keylo.security.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    private User getUserFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtUtils.extractEmail(token);
        return userService.loadUserByEmail(email);
    }

    @PostMapping
    public ResponseEntity<?> saveCredential(
            @RequestBody CredentialRequest request,
            HttpServletRequest httpRequest) {
        try {
            User user = getUserFromRequest(httpRequest);
            CredentialResponse saved = credentialService.saveCredential(request, user);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving credential: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getCredentials(HttpServletRequest httpRequest) {
        try {
            User user = getUserFromRequest(httpRequest);
            List<CredentialResponse> credentials = credentialService.getCredentialsForUser(user.getId());
            return ResponseEntity.ok(credentials);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving credentials: " + e.getMessage());
        }
    }

    @DeleteMapping("/{credentialId}")
    public ResponseEntity<?> deleteCredential(
            @PathVariable Long credentialId,
            HttpServletRequest httpRequest) {
        try {
            User user = getUserFromRequest(httpRequest);
            boolean deleted = credentialService.deleteCredential(credentialId, user.getId());
            
            if (deleted) {
                return ResponseEntity.ok("Credential deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting credential: " + e.getMessage());
        }
    }

    @PutMapping("/{credentialId}")
    public ResponseEntity<?> updateCredential(
            @PathVariable Long credentialId,
            @RequestBody CredentialRequest request,
            HttpServletRequest httpRequest) {
        try {
            User user = getUserFromRequest(httpRequest);
            CredentialResponse updated = credentialService.updateCredential(credentialId, request, user.getId());
            
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating credential: " + e.getMessage());
        }
    }
}