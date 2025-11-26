package com.appdevg4.CitMedConnect.service;

import com.appdevg4.CitMedConnect.entity.AppointmentEntity;
import com.appdevg4.CitMedConnect.entity.UserEntity;
import com.appdevg4.CitMedConnect.entity.TimeSlot;
import com.appdevg4.CitMedConnect.repository.AppointmentRepository;
import com.appdevg4.CitMedConnect.repository.UserRepository;
import com.appdevg4.CitMedConnect.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    // Create appointment
    public AppointmentEntity createAppointment(AppointmentEntity appointment) {
        // Validate user exists
        if (appointment.getUser() != null) {
            UserEntity user = userRepository.findById(appointment.getUser().getSchool_Id()).orElse(null);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            appointment.setUser(user);
        }
        
        // Validate time slot exists and is available
        if (appointment.getTimeSlot() != null) {
            TimeSlot timeSlot = timeSlotRepository.findById(appointment.getTimeSlot().getTimeSlotId()).orElse(null);
            if (timeSlot == null) {
                throw new RuntimeException("Time slot not found");
            }
            if (!timeSlot.isAvailable()) {
                throw new RuntimeException("Time slot is not available");
            }
            appointment.setTimeSlot(timeSlot);
            
            // Update time slot bookings
            timeSlot.setCurrentBookings(timeSlot.getCurrentBookings() + 1);
            if (timeSlot.getCurrentBookings() >= timeSlot.getMaxBookings()) {
                timeSlot.setAvailable(false);
            }
            timeSlotRepository.save(timeSlot);
        }
        
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        return appointmentRepository.save(appointment);
    }
    
    // Get all appointments
    public List<AppointmentEntity> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    // Get appointment by ID
    public AppointmentEntity getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }
    
    // Get appointments by user ID
    public List<AppointmentEntity> getAppointmentsByUserId(String userId) {
        return appointmentRepository.findByUser_SchoolId(userId);
    }
    
    // Get appointments by status
    public List<AppointmentEntity> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }
    
    // Get appointments by time slot ID
    public List<AppointmentEntity> getAppointmentsByTimeSlotId(Long timeSlotId) {
        return appointmentRepository.findByTimeSlot_TimeSlotId(timeSlotId);
    }
    
    // Update appointment
    public AppointmentEntity updateAppointment(Long id, AppointmentEntity appointmentDetails) {
        return appointmentRepository.findById(id).map(existingAppointment -> {
            if (appointmentDetails.getStatus() != null) {
                String oldStatus = existingAppointment.getStatus();
                existingAppointment.setStatus(appointmentDetails.getStatus());
                
                // If cancelling, free up the time slot
                if ("CANCELLED".equals(appointmentDetails.getStatus()) && !"CANCELLED".equals(oldStatus)) {
                    TimeSlot timeSlot = existingAppointment.getTimeSlot();
                    if (timeSlot != null) {
                        timeSlot.setCurrentBookings(Math.max(0, timeSlot.getCurrentBookings() - 1));
                        timeSlot.setAvailable(true);
                        timeSlotRepository.save(timeSlot);
                    }
                }
            }
            if (appointmentDetails.getReason() != null) {
                existingAppointment.setReason(appointmentDetails.getReason());
            }
            if (appointmentDetails.getNotes() != null) {
                existingAppointment.setNotes(appointmentDetails.getNotes());
            }
            
            existingAppointment.setUpdatedAt(LocalDateTime.now());
            return appointmentRepository.save(existingAppointment);
        }).orElse(null);
    }
    
    // Delete appointment
    public ResponseEntity<Map<String, Boolean>> deleteAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            // Free up the time slot
            TimeSlot timeSlot = appointment.getTimeSlot();
            if (timeSlot != null && !"CANCELLED".equals(appointment.getStatus())) {
                timeSlot.setCurrentBookings(Math.max(0, timeSlot.getCurrentBookings() - 1));
                timeSlot.setAvailable(true);
                timeSlotRepository.save(timeSlot);
            }
            
            appointmentRepository.delete(appointment);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }
    
    // Cancel appointment
    public AppointmentEntity cancelAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            if (!"CANCELLED".equals(appointment.getStatus())) {
                appointment.setStatus("CANCELLED");
                
                // Free up the time slot
                TimeSlot timeSlot = appointment.getTimeSlot();
                if (timeSlot != null) {
                    timeSlot.setCurrentBookings(Math.max(0, timeSlot.getCurrentBookings() - 1));
                    timeSlot.setAvailable(true);
                    timeSlotRepository.save(timeSlot);
                }
                
                appointment.setUpdatedAt(LocalDateTime.now());
                return appointmentRepository.save(appointment);
            }
            return appointment;
        }).orElse(null);
    }
    
    // Confirm appointment
    public AppointmentEntity confirmAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus("CONFIRMED");
            appointment.setUpdatedAt(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }).orElse(null);
    }
    
    // Complete appointment
    public AppointmentEntity completeAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus("COMPLETED");
            appointment.setUpdatedAt(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }).orElse(null);
    }
}
