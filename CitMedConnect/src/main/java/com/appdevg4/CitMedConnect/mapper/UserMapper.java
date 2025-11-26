package com.appdevg4.CitMedConnect.mapper;

import com.appdevg4.CitMedConnect.dto.UserDTO;
import com.appdevg4.CitMedConnect.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    
    public UserDTO toDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setSchoolId(entity.getSchool_Id());
        dto.setFirstName(entity.getFirst_Name());
        dto.setLastName(entity.getLast_Name());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setGender(entity.getGender());
        dto.setAge(entity.getAge());
        dto.setCreatedAt(entity.getCreated_At());
        
        // Note: Password is NOT included in DTO for security
        
        return dto;
    }
    
    // Convert DTO to Entity (for create operations)
    public UserEntity toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        UserEntity entity = new UserEntity();
        
        // Don't set schoolId as it's auto-generated or set by database
        entity.setFirst_Name(dto.getFirstName());
        entity.setLast_Name(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        entity.setGender(dto.getGender());
        entity.setAge(dto.getAge());
        
        // Password should be set separately with proper hashing
        
        return entity;
    }
    
    // Update existing entity with DTO data
    public void updateEntityFromDTO(UserDTO dto, UserEntity entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        if (dto.getFirstName() != null) {
            entity.setFirst_Name(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLast_Name(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }
        if (dto.getGender() != null) {
            entity.setGender(dto.getGender());
        }
        if (dto.getAge() > 0) {
            entity.setAge(dto.getAge());
        }
        
        // Password updates should be handled separately with proper validation
    }
}