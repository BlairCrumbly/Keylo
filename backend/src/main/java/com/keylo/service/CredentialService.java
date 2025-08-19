package com.keylo.service;

import com.keylo.dto.CredentialRequest;
import com.keylo.dto.CredentialResponse;
import com.keylo.model.Credential;
import com.keylo.model.User;
import com.keylo.repository.CredentialRepository;
import com.keylo.security.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;

    @Autowired
    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public CredentialResponse saveCredential(CredentialRequest dto, User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User must not be null and must have a valid ID");
        }

        Credential credential = new Credential();
        credential.setUser(user);
        credential.setSiteName(Optional.ofNullable(dto.getSiteName()).orElse(""));
        credential.setSiteUrl(Optional.ofNullable(dto.getSiteUrl()).orElse(""));
        credential.setLoginEmail(Optional.ofNullable(dto.getLoginEmail()).orElse(""));
        credential.setLoginUsername(Optional.ofNullable(dto.getLoginUsername()).orElse(""));
        
        // Encrypt the password before saving
        String rawPassword = Optional.ofNullable(dto.getLoginPassword()).orElse("");
        if (!rawPassword.isEmpty()) {
            credential.setLoginPassword(EncryptionUtil.encrypt(rawPassword));
        } else {
            credential.setLoginPassword("");
        }
        
        credential.setDescription(Optional.ofNullable(dto.getDescription()).orElse(""));

        Credential saved = credentialRepository.save(credential);

        System.out.println("Saving credential for user ID: " + user.getId());
        System.out.println("Site: " + dto.getSiteName());
        System.out.println("Email: " + dto.getLoginEmail());
        System.out.println("Saved credential ID: " + saved.getId());

        return mapToResponse(saved);
    }

    public List<CredentialResponse> getCredentialsForUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        List<Credential> credentials = credentialRepository.findByUserId(userId);
        return credentials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteCredential(Long credentialId, Long userId) {
        if (credentialId == null || userId == null) {
            throw new IllegalArgumentException("Credential ID and User ID must not be null");
        }

        Optional<Credential> credentialOpt = credentialRepository.findById(credentialId);
        
        if (credentialOpt.isPresent()) {
            Credential credential = credentialOpt.get();
            
            // Verify that the credential belongs to the user
            if (!credential.getUser().getId().equals(userId)) {
                throw new SecurityException("User does not have permission to delete this credential");
            }
            
            credentialRepository.deleteById(credentialId);
            return true;
        }
        
        return false; // Credential not found
    }

    public CredentialResponse updateCredential(Long credentialId, CredentialRequest dto, Long userId) {
        if (credentialId == null || userId == null) {
            throw new IllegalArgumentException("Credential ID and User ID must not be null");
        }

        Optional<Credential> credentialOpt = credentialRepository.findById(credentialId);
        
        if (credentialOpt.isPresent()) {
            Credential credential = credentialOpt.get();
            
            // Verify that the credential belongs to the user
            if (!credential.getUser().getId().equals(userId)) {
                throw new SecurityException("User does not have permission to update this credential");
            }
            
            // Update fields
            credential.setSiteName(Optional.ofNullable(dto.getSiteName()).orElse(""));
            credential.setSiteUrl(Optional.ofNullable(dto.getSiteUrl()).orElse(""));
            credential.setLoginEmail(Optional.ofNullable(dto.getLoginEmail()).orElse(""));
            credential.setLoginUsername(Optional.ofNullable(dto.getLoginUsername()).orElse(""));
            
            // Encrypt the password if provided
            String rawPassword = dto.getLoginPassword();
            if (rawPassword != null && !rawPassword.isEmpty()) {
                credential.setLoginPassword(EncryptionUtil.encrypt(rawPassword));
            }
            
            credential.setDescription(Optional.ofNullable(dto.getDescription()).orElse(""));
            
            Credential updated = credentialRepository.save(credential);
            return mapToResponse(updated);
        }
        
        return null; // Credential not found
    }

    private CredentialResponse mapToResponse(Credential credential) {
        CredentialResponse response = new CredentialResponse();
        response.setId(credential.getId());
        response.setSiteName(credential.getSiteName());
        response.setSiteUrl(credential.getSiteUrl());
        response.setLoginEmail(credential.getLoginEmail());
        response.setLoginUsername(credential.getLoginUsername());
        
        // Decrypt the password when returning it
        String encryptedPassword = credential.getLoginPassword();
        if (encryptedPassword != null && !encryptedPassword.isEmpty()) {
            try {
                response.setLoginPassword(EncryptionUtil.decrypt(encryptedPassword));
            } catch (Exception e) {
                System.err.println("Error decrypting password for credential " + credential.getId() + ": " + e.getMessage());
                response.setLoginPassword(""); // Return empty string if decryption fails
            }
        } else {
            response.setLoginPassword("");
        }
        
        response.setDescription(credential.getDescription());
        response.setCreatedAt(credential.getCreatedAt());
        return response;
    }
}