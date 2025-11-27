package com.appdevg4.CitMedConnect.controller;

import com.appdevg4.CitMedConnect.entity.AppointmentEntity;
import com.appdevg4.CitMedConnect.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @PostMapping("/")
    public AppointmentEntity createAppointment(@RequestBody AppointmentEntity appointment) {
        return appointmentService.createAppointment(appointment);
    }
    
    @GetMapping("/")
    public List<AppointmentEntity> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
    
    @GetMapping("/{id}")
    public AppointmentEntity getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }
    
    @GetMapping("/user/{userId}")
    public List<AppointmentEntity> getAppointmentsByUserId(@PathVariable String userId) {
        return appointmentService.getAppointmentsByUserId(userId);
    }
    
    @GetMapping("/status/{status}")
    public List<AppointmentEntity> getAppointmentsByStatus(@PathVariable String status) {
        return appointmentService.getAppointmentsByStatus(status);
    }
    
    @GetMapping("/timeslot/{timeSlotId}")
    public List<AppointmentEntity> getAppointmentsByTimeSlotId(@PathVariable Long timeSlotId) {
        return appointmentService.getAppointmentsByTimeSlotId(timeSlotId);
    }
    
    @PutMapping("/{id}")
    public AppointmentEntity updateAppointment(@PathVariable Long id, @RequestBody AppointmentEntity appointmentDetails) {
        return appointmentService.updateAppointment(id, appointmentDetails);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAppointment(@PathVariable Long id) {
        return appointmentService.deleteAppointment(id);
    }
    
    @PutMapping("/{id}/cancel")
    public AppointmentEntity cancelAppointment(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }
    
    @PutMapping("/{id}/confirm")
    public AppointmentEntity confirmAppointment(@PathVariable Long id) {
        return appointmentService.confirmAppointment(id);
    }
    
    @PutMapping("/{id}/complete")
    public AppointmentEntity completeAppointment(@PathVariable Long id) {
        return appointmentService.completeAppointment(id);
    }
}
