package com.appdevg4.CitMedConnect.controller;

import com.appdevg4.CitMedConnect.dto.MedicalRecordDTO;
import com.appdevg4.CitMedConnect.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDTO) {
        try {
            MedicalRecordDTO createdRecord = medicalRecordService.createMedicalRecord(medicalRecordDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    // Get all medical records
    @GetMapping("/")
    public ResponseEntity<List<MedicalRecordDTO>> getAllMedicalRecords() {
        List<MedicalRecordDTO> records = medicalRecordService.getAllMedicalRecords();
        return ResponseEntity.ok(records);
    }
    
    // Get medical record by ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Long id) {
        MedicalRecordDTO record = medicalRecordService.getMedicalRecordById(id);
        if (record != null) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Get medical records by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByUserId(@PathVariable String userId) {
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByUserId(userId);
        return ResponseEntity.ok(records);
    }
    
    // Get medical records by user ID sorted by date
    @GetMapping("/user/{userId}/sorted")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByUserIdSorted(@PathVariable String userId) {
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByUserIdSorted(userId);
        return ResponseEntity.ok(records);
    }
    
    // Get medical record by appointment ID
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordByAppointmentId(@PathVariable Long appointmentId) {
        MedicalRecordDTO record = medicalRecordService.getMedicalRecordByAppointmentId(appointmentId);
        if (record != null) {
            return ResponseEntity.ok(record);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Get medical records by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByDateRange(startDate, endDate);
        return ResponseEntity.ok(records);
    }
    
    // Get medical records by user and date range
    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByUserAndDateRange(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByUserAndDateRange(userId, startDate, endDate);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // Get medical records by created by
    @GetMapping("/created-by/{createdBy}")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByCreatedBy(@PathVariable String createdBy) {
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByCreatedBy(createdBy);
        return ResponseEntity.ok(records);
    }
    
    // Update medical record
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
            @PathVariable Long id, 
            @RequestBody MedicalRecordDTO recordDetails) {
        try {
            MedicalRecordDTO updatedRecord = medicalRecordService.updateMedicalRecord(id, recordDetails);
            return ResponseEntity.ok(updatedRecord);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // Delete medical record
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteMedicalRecord(@PathVariable Long id) {
        return medicalRecordService.deleteMedicalRecord(id);
    }
    
    // Get count of records by user
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getRecordCountByUserId(@PathVariable String userId) {
        long count = medicalRecordService.getRecordCountByUserId(userId);
        return ResponseEntity.ok(count);
    }
}