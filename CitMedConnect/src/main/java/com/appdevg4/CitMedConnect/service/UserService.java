package com.appdevg4.CitMedConnect.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.appdevg4.CitMedConnect.dto.UserDTO;
import com.appdevg4.CitMedConnect.entity.UserEntity;
import com.appdevg4.CitMedConnect.mapper.UserMapper;
import com.appdevg4.CitMedConnect.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper mapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + userDTO.getEmail());
        }
        
        UserEntity entity = mapper.toEntity(userDTO);
        
        if (userDTO.getPassword() != null) {
            entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        UserEntity savedEntity = userRepository.save(entity);
        return mapper.toDTO(savedEntity);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public UserDTO getUserById(String id) {
        UserEntity entity = userRepository.findById(id).orElse(null);
        return mapper.toDTO(entity);
    }
    
    public UserDTO updateUser(String id, UserDTO userDTO) {
        UserEntity existingEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        
        mapper.updateEntityFromDTO(userDTO, existingEntity);
        UserEntity updatedEntity = userRepository.save(existingEntity);
        
        return mapper.toDTO(updatedEntity);
    }
    
    public ResponseEntity<Map<String, Boolean>> deleteUser(String id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }
    
    public UserDTO getUserByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email);
        return mapper.toDTO(entity);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public UserDTO findByEmail(String email) {
        UserEntity entity = userRepository.findByEmail(email);
        return mapper.toDTO(entity);
    }
}