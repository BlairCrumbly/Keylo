package com.keylo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;

import com.keylo.dto.LoginRequest;
import com.keylo.dto.JwtResponse;
import com.keylo.model.User;
import com.keylo.repository.UserRepository;
import com.keylo.security.JwtUtils;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //! check if email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    //! register new user with encrypted password
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //! req login logic
    // 1. retrieve user by email
    // 2. check password
    // 3. generate JWT
    // 4. return token + user info
    public JwtResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        return new JwtResponse(token, user.getId(), user.getEmail());
    }

    //! gets and sets

    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User updateUser(User user) {
        if (!user.getPassword().startsWith("$2a$")) { // bcrypt hashes start with $2a$
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
    public User loadUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
