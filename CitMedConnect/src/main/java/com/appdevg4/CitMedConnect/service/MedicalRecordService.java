package com.appdevg4.CitMedConnect.service;

import com.appdevg4.CitMedConnect.entity.MedicalRecordEntity;
import com.appdevg4.CitMedConnect.entity.UserEntity;
import com.appdevg4.CitMedConnect.entity.AppointmentEntity;
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

@Service
public class MedicalRecordService {
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    // Create medical record
    public MedicalRecordEntity createMedicalRecord(MedicalRecordEntity medicalRecord) {
        // Validate user exists
        if (medicalRecord.getUser() != null) {
            UserEntity user = userRepository.findById(medicalRecord.getUser().getSchool_Id()).orElse(null);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            medicalRecord.setUser(user);
        }
        
        // Validate appointment exists if provided
        if (medicalRecord.getAppointment() != null && medicalRecord.getAppointment().getAppointmentId() != null) {
            AppointmentEntity appointment = appointmentRepository.findById(medicalRecord.getAppointment().getAppointmentId()).orElse(null);
            if (appointment == null) {
                throw new RuntimeException("Appointment not found");
            }
            medicalRecord.setAppointment(appointment);
            
            // Optionally mark appointment as completed
            if (!"COMPLETED".equals(appointment.getStatus())) {
                appointment.setStatus("COMPLETED");
                appointmentRepository.save(appointment);
            }
        }
        
        medicalRecord.setCreatedAt(LocalDateTime.now());
        medicalRecord.setUpdatedAt(LocalDateTime.now());
        if (medicalRecord.getRecordDate() == null) {
            medicalRecord.setRecordDate(LocalDateTime.now());
        }
        
        return medicalRecordRepository.save(medicalRecord);
    }
    
    // Get all medical records
    public List<MedicalRecordEntity> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }
    
    // Get medical record by ID
    public MedicalRecordEntity getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id).orElse(null);
    }
    
    // Get medical records by user ID
    public List<MedicalRecordEntity> getMedicalRecordsByUserId(String userId) {
        return medicalRecordRepository.findByUser_SchoolId(userId);
    }
    
    // Get medical records by user ID ordered by date
    public List<MedicalRecordEntity> getMedicalRecordsByUserIdSorted(String userId) {
        return medicalRecordRepository.findByUser_SchoolIdOrderByRecordDateDesc(userId);
    }
    
    // Get medical record by appointment ID
    public MedicalRecordEntity getMedicalRecordByAppointmentId(Long appointmentId) {
        return medicalRecordRepository.findByAppointment_AppointmentId(appointmentId);
    }
    
    // Get medical records by date range
    public List<MedicalRecordEntity> getMedicalRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return medicalRecordRepository.findByRecordDateBetween(startDate, endDate);
    }
    
    // Get medical records by user and date range
    public List<MedicalRecordEntity> getMedicalRecordsByUserAndDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return medicalRecordRepository.findByUserAndRecordDateBetween(user, startDate, endDate);
    }
    
    // Get medical records by created by
    public List<MedicalRecordEntity> getMedicalRecordsByCreatedBy(String createdBy) {
        return medicalRecordRepository.findByCreatedBy(createdBy);
    }
    
    // Update medical record
    public MedicalRecordEntity updateMedicalRecord(Long id, MedicalRecordEntity recordDetails) {
        return medicalRecordRepository.findById(id).map(existingRecord -> {
            if (recordDetails.getDiagnosis() != null) {
                existingRecord.setDiagnosis(recordDetails.getDiagnosis());
            }
            if (recordDetails.getSymptoms() != null) {
                existingRecord.setSymptoms(recordDetails.getSymptoms());
            }
            if (recordDetails.getTreatment() != null) {
                existingRecord.setTreatment(recordDetails.getTreatment());
            }
            if (recordDetails.getPrescription() != null) {
                existingRecord.setPrescription(recordDetails.getPrescription());
            }
            if (recordDetails.getVitalSigns() != null) {
                existingRecord.setVitalSigns(recordDetails.getVitalSigns());
            }
            if (recordDetails.getAllergies() != null) {
                existingRecord.setAllergies(recordDetails.getAllergies());
            }
            if (recordDetails.getMedicalHistory() != null) {
                existingRecord.setMedicalHistory(recordDetails.getMedicalHistory());
            }
            if (recordDetails.getNotes() != null) {
                existingRecord.setNotes(recordDetails.getNotes());
            }
            if (recordDetails.getRecordDate() != null) {
                existingRecord.setRecordDate(recordDetails.getRecordDate());
            }
            if (recordDetails.getCreatedBy() != null) {
                existingRecord.setCreatedBy(recordDetails.getCreatedBy());
            }
            
            existingRecord.setUpdatedAt(LocalDateTime.now());
            return medicalRecordRepository.save(existingRecord);
        }).orElse(null);
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
