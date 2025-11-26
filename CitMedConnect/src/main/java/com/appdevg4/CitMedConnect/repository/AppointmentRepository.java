package com.appdevg4.CitMedConnect.repository;

import com.appdevg4.CitMedConnect.entity.AppointmentEntity;
import com.appdevg4.CitMedConnect.entity.UserEntity;
import com.appdevg4.CitMedConnect.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    
    // Find appointments by user
    List<AppointmentEntity> findByUser(UserEntity user);
    
    // Find appointments by user ID
    List<AppointmentEntity> findByUser_SchoolId(String schoolId);
    
    // Find appointments by status
    List<AppointmentEntity> findByStatus(String status);
    
    // Find appointments by time slot
    List<AppointmentEntity> findByTimeSlot(TimeSlot timeSlot);
    
    // Find appointments by time slot ID
    List<AppointmentEntity> findByTimeSlot_TimeSlotId(Long timeSlotId);
    
    // Find appointments by user and status
    List<AppointmentEntity> findByUserAndStatus(UserEntity user, String status);
    
    // Check if user has appointment with specific time slot
    boolean existsByUserAndTimeSlot(UserEntity user, TimeSlot timeSlot);
    
    // Count appointments by time slot
    long countByTimeSlot(TimeSlot timeSlot);
}
