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
    
    public AppointmentEntity createAppointment(AppointmentEntity appointment) {
        if (appointment.getUser() != null) {
            UserEntity user = userRepository.findById(appointment.getUser().getSchoolId()).orElse(null);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            appointment.setUser(user);
        }
        
        if (appointment.getTimeSlot() != null) {
            TimeSlot timeSlot = timeSlotRepository.findById(appointment.getTimeSlot().getTimeSlotId()).orElse(null);
            if (timeSlot == null) {
                throw new RuntimeException("Time slot not found");
            }
            if (!timeSlot.isAvailable()) {
                throw new RuntimeException("Time slot is not available");
            }
            appointment.setTimeSlot(timeSlot);
            
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
    
    public List<AppointmentEntity> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public AppointmentEntity getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }
    
    public List<AppointmentEntity> getAppointmentsByUserId(String userId) {
        return appointmentRepository.findByUser_SchoolId(userId);
    }
    
    public List<AppointmentEntity> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }
    
    public List<AppointmentEntity> getAppointmentsByTimeSlotId(Long timeSlotId) {
        return appointmentRepository.findByTimeSlot_TimeSlotId(timeSlotId);
    }
    
    public AppointmentEntity updateAppointment(Long id, AppointmentEntity appointmentDetails) {
        return appointmentRepository.findById(id).map(existingAppointment -> {
            if (appointmentDetails.getStatus() != null) {
                String oldStatus = existingAppointment.getStatus();
                existingAppointment.setStatus(appointmentDetails.getStatus());
                
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
    
    public ResponseEntity<Map<String, Boolean>> deleteAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
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
    
    public AppointmentEntity cancelAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            if (!"CANCELLED".equals(appointment.getStatus())) {
                appointment.setStatus("CANCELLED");
                
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
    
    public AppointmentEntity confirmAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus("CONFIRMED");
            appointment.setUpdatedAt(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }).orElse(null);
    }
    
    public AppointmentEntity completeAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus("COMPLETED");
            appointment.setUpdatedAt(LocalDateTime.now());
            return appointmentRepository.save(appointment);
        }).orElse(null);
    }
    
    public AppointmentEntity bookAppointment(Long timeSlotId, String studentId, String reason, String notes) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
            .orElseThrow(() -> new IllegalArgumentException("Time slot not found"));
        
        if (!timeSlot.isAvailable()) {
            throw new IllegalStateException("Time slot is not available");
        }
        
        if (timeSlot.getCurrentBookings() >= timeSlot.getMaxBookings()) {
            throw new IllegalStateException("Time slot is fully booked");
        }
        
        UserEntity user = userRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUser(user);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus("PENDING");
        appointment.setReason(reason);
        appointment.setNotes(notes);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        
        timeSlot.setCurrentBookings(timeSlot.getCurrentBookings() + 1);
        if (timeSlot.getCurrentBookings() >= timeSlot.getMaxBookings()) {
            timeSlot.setAvailable(false);
        }
        timeSlotRepository.save(timeSlot);
        
        return appointmentRepository.save(appointment);
    }
    
    public AppointmentEntity rescheduleAppointment(Long appointmentId, Long newTimeSlotId) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        
        TimeSlot newTimeSlot = timeSlotRepository.findById(newTimeSlotId)
            .orElseThrow(() -> new IllegalArgumentException("New time slot not found"));
        
        if (!newTimeSlot.isAvailable()) {
            throw new IllegalStateException("New time slot is not available");
        }
        
        if (newTimeSlot.getCurrentBookings() >= newTimeSlot.getMaxBookings()) {
            throw new IllegalStateException("New time slot is fully booked");
        }
        
        TimeSlot oldTimeSlot = appointment.getTimeSlot();
        
        if (oldTimeSlot != null) {
            oldTimeSlot.setCurrentBookings(Math.max(0, oldTimeSlot.getCurrentBookings() - 1));
            oldTimeSlot.setAvailable(true);
            timeSlotRepository.save(oldTimeSlot);
        }
        
        appointment.setTimeSlot(newTimeSlot);
        appointment.setStatus("RESCHEDULED");
        appointment.setUpdatedAt(LocalDateTime.now());
        
        newTimeSlot.setCurrentBookings(newTimeSlot.getCurrentBookings() + 1);
        if (newTimeSlot.getCurrentBookings() >= newTimeSlot.getMaxBookings()) {
            newTimeSlot.setAvailable(false);
        }
        timeSlotRepository.save(newTimeSlot);
        
        return appointmentRepository.save(appointment);
    }
}
