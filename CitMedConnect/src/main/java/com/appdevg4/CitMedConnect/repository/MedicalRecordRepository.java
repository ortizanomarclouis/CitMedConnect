package com.appdevg4.CitMedConnect.repository;

import com.appdevg4.CitMedConnect.entity.MedicalRecordEntity;
import com.appdevg4.CitMedConnect.entity.UserEntity;
import com.appdevg4.CitMedConnect.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {
    
    // Find medical records by user
    List<MedicalRecordEntity> findByUser(UserEntity user);
    
    // Find medical records by user ID
    List<MedicalRecordEntity> findByUser_SchoolId(String schoolId);
    
    // Find medical records by appointment
    List<MedicalRecordEntity> findByAppointment(AppointmentEntity appointment);
    
    // Find medical records by appointment ID
    MedicalRecordEntity findByAppointment_AppointmentId(Long appointmentId);
    
    // Find medical records by user and date range
    List<MedicalRecordEntity> findByUserAndRecordDateBetween(UserEntity user, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find medical records by date range
    List<MedicalRecordEntity> findByRecordDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find medical records by created by
    List<MedicalRecordEntity> findByCreatedBy(String createdBy);
    
    // Find most recent records for a user
    List<MedicalRecordEntity> findByUserOrderByRecordDateDesc(UserEntity user);
    
    // Find most recent records by user ID
    List<MedicalRecordEntity> findByUser_SchoolIdOrderByRecordDateDesc(String schoolId);
    
    // Count records by user
    long countByUser(UserEntity user);
}

