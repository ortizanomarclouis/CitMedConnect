package com.appdevg4.CitMedConnect.controller;

import com.appdevg4.CitMedConnect.entity.TimeSlot;
import com.appdevg4.CitMedConnect.entity.AppointmentEntity;
import com.appdevg4.CitMedConnect.service.TimeSlotService;
import com.appdevg4.CitMedConnect.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timeslots")
@CrossOrigin(origins = "*")
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<TimeSlot> createSlot(@RequestBody TimeSlot slot) {
        if (slot.getMaxBookings() <= 0) {
            slot.setMaxBookings(1);
        }
        if (slot.getCurrentBookings() == 0) {
            slot.setCurrentBookings(0);
        }
        if (slot.isAvailable() == false) {
            slot.setAvailable(true);
        }
        if (slot.getStartTime() != null && slot.getEndTime() != null) {
            LocalTime start = slot.getStartTime();
            LocalTime end = slot.getEndTime();
            boolean isBusinessHours = !start.isBefore(LocalTime.of(8, 0)) && 
                                     !end.isAfter(LocalTime.of(18, 0));
            slot.setWithinBusinessHours(isBusinessHours);
        } else {
            slot.setWithinBusinessHours(true);
        }
        
        TimeSlot createdSlot = timeSlotService.createSlot(slot);
        return ResponseEntity.ok(createdSlot);
    }

    @GetMapping
    public List<TimeSlot> getAllSlots() {
        return timeSlotService.getAllSlots();
    }

    @GetMapping("/available")
    public ResponseEntity<List<TimeSlot>> getAvailableSlotsByDate(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<TimeSlot> availableSlots = timeSlotService.findAvailableSlots(date);
        return ResponseEntity.ok(availableSlots);
    }

    @GetMapping("/staff/{staffId}")
    public List<TimeSlot> getSlotsByStaff(@PathVariable String staffId) {
        return timeSlotService.getStaffSlots(staffId);
    }

    @GetMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlot> getSlotById(@PathVariable Long timeSlotId) {
        TimeSlot slot = timeSlotService.getSlotById(timeSlotId);
        return ResponseEntity.ofNullable(slot);
    }

    @PutMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlot> updateSlot(
            @PathVariable Long timeSlotId, 
            @RequestBody TimeSlot slot) {
        TimeSlot updated = timeSlotService.updateSlot(timeSlotId, slot);
        return updated != null ? 
               ResponseEntity.ok(updated) : 
               ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{timeSlotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long timeSlotId) {
        if (timeSlotService.deleteSlot(timeSlotId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{timeSlotId}/book")
    public ResponseEntity<AppointmentEntity> bookTimeSlot(
            @PathVariable Long timeSlotId,
            @RequestBody Map<String, String> bookingDetails) {
        try {
            String studentId = bookingDetails.get("studentId");
            String reason = bookingDetails.get("reason");
            String notes = bookingDetails.get("notes");
            
            if (studentId == null || studentId.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            AppointmentEntity appointment = appointmentService.bookAppointment(
                timeSlotId, studentId, reason, notes);
            return ResponseEntity.ok(appointment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}