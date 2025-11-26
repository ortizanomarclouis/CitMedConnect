package com.appdevg4.CitMedConnect.service;

import com.appdevg4.CitMedConnect.dto.MedicalRecordDTO;
import com.appdevg4.CitMedConnect.entity.MedicalRecordEntity;
import com.appdevg4.CitMedConnect.entity.UserEntity;
import com.appdevg4.CitMedConnect.entity.AppointmentEntity;
import com.appdevg4.CitMedConnect.mapper.MedicalRecordMapper;
import com.appdevg4.CitMedConnect.repository.MedicalRecordRepository;
import com.appdevg4.CitMedConnect.repository.UserRepository;
import com.appdevg4.CitMedConnect.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private MedicalRecordMapper mapper;
    
    // Create medical record
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto) {
        // Convert DTO to Entity
        MedicalRecordEntity entity = mapper.toEntity(dto);
        
        // Validate and set user
        if (dto.getUserId() != null) {
            UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getUserId()));
            entity.setUser(user);
        }
        
        // Validate and set appointment if provided
        if (dto.getAppointmentId() != null) {
            AppointmentEntity appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + dto.getAppointmentId()));
            entity.setAppointment(appointment);
            
            // Mark appointment as completed
            if (!"COMPLETED".equals(appointment.getStatus())) {
                appointment.setStatus("COMPLETED");
                appointmentRepository.save(appointment);
            }
        }
        
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        if (entity.getRecordDate() == null) {
            entity.setRecordDate(LocalDateTime.now());
        }
        
        MedicalRecordEntity savedEntity = medicalRecordRepository.save(entity);
        return mapper.toDTO(savedEntity);
    }
    
    // Get all medical records
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get medical record by ID
    public MedicalRecordDTO getMedicalRecordById(Long id) {
        MedicalRecordEntity entity = medicalRecordRepository.findById(id).orElse(null);
        return mapper.toDTO(entity);
    }
    
    // Get medical records by user ID
    public List<MedicalRecordDTO> getMedicalRecordsByUserId(String userId) {
        return medicalRecordRepository.findByUser_SchoolId(userId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get medical records by user ID sorted by date
    public List<MedicalRecordDTO> getMedicalRecordsByUserIdSorted(String userId) {
        return medicalRecordRepository.findByUser_SchoolIdOrderByRecordDateDesc(userId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get medical record by appointment ID
    public MedicalRecordDTO getMedicalRecordByAppointmentId(Long appointmentId) {
        MedicalRecordEntity entity = medicalRecordRepository.findByAppointment_AppointmentId(appointmentId);
        return mapper.toDTO(entity);
    }
    
    // Get medical records by date range
    public List<MedicalRecordDTO> getMedicalRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return medicalRecordRepository.findByRecordDateBetween(startDate, endDate).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get medical records by user and date range
    public List<MedicalRecordDTO> getMedicalRecordsByUserAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return medicalRecordRepository.findByUserAndRecordDateBetween(user, startDate, endDate).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Get medical records by created by
    public List<MedicalRecordDTO> getMedicalRecordsByCreatedBy(String createdBy) {
        return medicalRecordRepository.findByCreatedBy(createdBy).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Update medical record
    public MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO dto) {
        MedicalRecordEntity existingEntity = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found with ID: " + id));
        
        mapper.updateEntityFromDTO(dto, existingEntity);
        existingEntity.setUpdatedAt(LocalDateTime.now());
        
        MedicalRecordEntity updatedEntity = medicalRecordRepository.save(existingEntity);
        return mapper.toDTO(updatedEntity);
    }
    
    // Delete medical record
    public ResponseEntity<Map<String, Boolean>> deleteMedicalRecord(Long id) {
        return medicalRecordRepository.findById(id).map(record -> {
            medicalRecordRepository.delete(record);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }
    
    // Get count of records by user
    public long getRecordCountByUserId(String userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0;
        }
        return medicalRecordRepository.countByUser(user);
    }
}