package com.appdevg4.CitMedConnect.controller;

import com.appdevg4.CitMedConnect.entity.MedicalRecordEntity;
import com.appdevg4.CitMedConnect.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medical-records")
@CrossOrigin(origins = "*")
public class MedicalRecordController {
    
    @Autowired
    private MedicalRecordService medicalRecordService;
    
    // Create new medical record
    @PostMapping("/")
    public MedicalRecordEntity createMedicalRecord(@RequestBody MedicalRecordEntity medicalRecord) {
        return medicalRecordService.createMedicalRecord(medicalRecord);
    }
    
    // Get all medical records
    @GetMapping("/")
    public List<MedicalRecordEntity> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }
    
    // Get medical record by ID
    @GetMapping("/{id}")
    public MedicalRecordEntity getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }
    
    // Get medical records by user ID
    @GetMapping("/user/{userId}")
    public List<MedicalRecordEntity> getMedicalRecordsByUserId(@PathVariable String userId) {
        return medicalRecordService.getMedicalRecordsByUserId(userId);
    }
    
    // Get medical records by user ID sorted by date
    @GetMapping("/user/{userId}/sorted")
    public List<MedicalRecordEntity> getMedicalRecordsByUserIdSorted(@PathVariable String userId) {
        return medicalRecordService.getMedicalRecordsByUserIdSorted(userId);
    }
    
    // Get medical record by appointment ID
    @GetMapping("/appointment/{appointmentId}")
    public MedicalRecordEntity getMedicalRecordByAppointmentId(@PathVariable Long appointmentId) {
        return medicalRecordService.getMedicalRecordByAppointmentId(appointmentId);
    }
    
    // Get medical records by date range
    @GetMapping("/date-range")
    public List<MedicalRecordEntity> getMedicalRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return medicalRecordService.getMedicalRecordsByDateRange(startDate, endDate);
    }
    
    // Get medical records by user and date range
    @GetMapping("/user/{userId}/date-range")
    public List<MedicalRecordEntity> getMedicalRecordsByUserAndDateRange(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return medicalRecordService.getMedicalRecordsByUserAndDateRange(userId, startDate, endDate);
    }
    
    // Get medical records by created by
    @GetMapping("/created-by/{createdBy}")
    public List<MedicalRecordEntity> getMedicalRecordsByCreatedBy(@PathVariable String createdBy) {
        return medicalRecordService.getMedicalRecordsByCreatedBy(createdBy);
    }
    
    // Update medical record
    @PutMapping("/{id}")
    public MedicalRecordEntity updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecordEntity recordDetails) {
        return medicalRecordService.updateMedicalRecord(id, recordDetails);
    }
    
    // Delete medical record
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteMedicalRecord(@PathVariable Long id) {
        return medicalRecordService.deleteMedicalRecord(id);
    }
    
    // Get count of records by user
    @GetMapping("/user/{userId}/count")
    public long getRecordCountByUserId(@PathVariable String userId) {
        return medicalRecordService.getRecordCountByUserId(userId);
    }
}

