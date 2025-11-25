package com.appdevg4.CitMedConnect.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.appdevg4.CitMedConnect.entity.UserEntity;
import com.appdevg4.CitMedConnect.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Create 
    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }
    
    // Read All
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Read By ID
    public UserEntity getUserById(String id) {
        return userRepository.findById(Integer.parseInt(id)).orElse(null);
    }
    
    // Update 
    public UserEntity updateUser(String id, UserEntity user) {
        return userRepository.findById(Integer.parseInt(id)).map(existingUser -> {
            // Update only the fields that should be updated
            if (user.getEmail() != null) existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null) existingUser.setPassword(user.getPassword());
            if (user.getFirst_Name() != null) existingUser.setFirst_Name(user.getFirst_Name());
            if (user.getLast_Name() != null) existingUser.setLast_Name(user.getLast_Name());
            if (user.getPhone() != null) existingUser.setPhone(user.getPhone());
            if (user.getRole() != null) existingUser.setRole(user.getRole());
            if (user.getGender() != null) existingUser.setGender(user.getGender());
            if (user.getAge() > 0) existingUser.setAge(user.getAge());
            
            return userRepository.save(existingUser);
        }).orElse(null);
    }
    
    // Delete 
    public ResponseEntity<Map<String, Boolean>> deleteUser(String id) {
        return userRepository.findById(Integer.parseInt(id)).map(user -> {
            userRepository.delete(user);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }
    
    // Get user by email
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Additional methods
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
