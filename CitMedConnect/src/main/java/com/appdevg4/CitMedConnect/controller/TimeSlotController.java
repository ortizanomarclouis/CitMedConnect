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

    // Create a new time slot (for staff)
    @PostMapping
    public ResponseEntity<TimeSlot> createSlot(@RequestBody TimeSlot slot) {
        // Set default values if not provided
        if (slot.getMaxBookings() <= 0) {
            slot.setMaxBookings(1); // Default to 1 booking per slot
        }
        if (slot.getCurrentBookings() == 0) {
            slot.setCurrentBookings(0);
        }
        if (slot.isAvailable() == false) {
            slot.setAvailable(true); // Default to available
        }
        // Set default business hours (assuming 8 AM - 6 PM are business hours)
        if (slot.getStartTime() != null && slot.getEndTime() != null) {
            LocalTime start = slot.getStartTime();
            LocalTime end = slot.getEndTime();
            // Check if slot is within business hours (8:00 AM to 6:00 PM)
            boolean isBusinessHours = !start.isBefore(LocalTime.of(8, 0)) && 
                                     !end.isAfter(LocalTime.of(18, 0));
            slot.setWithinBusinessHours(isBusinessHours);
        } else {
            slot.setWithinBusinessHours(true); // Default to true if times are not set
        }
        
        TimeSlot createdSlot = timeSlotService.createSlot(slot);
        return ResponseEntity.ok(createdSlot);
    }

    // Get all time slots (for admin/staff)
    @GetMapping
    public List<TimeSlot> getAllSlots() {
        return timeSlotService.getAllSlots();
    }

    // Get available time slots (for students)
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

    // Get time slots for a specific staff member
    @GetMapping("/staff/{staffId}")
    public List<TimeSlot> getSlotsByStaff(@PathVariable String staffId) {
        return timeSlotService.getStaffSlots(staffId);
    }

    // Get a specific time slot by ID
    @GetMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlot> getSlotById(@PathVariable Long timeSlotId) {
        TimeSlot slot = timeSlotService.getSlotById(timeSlotId);
        return ResponseEntity.ofNullable(slot);
    }

    // Update a time slot
    @PutMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlot> updateSlot(
            @PathVariable Long timeSlotId, 
            @RequestBody TimeSlot slot) {
        TimeSlot updated = timeSlotService.updateSlot(timeSlotId, slot);
        return updated != null ? 
               ResponseEntity.ok(updated) : 
               ResponseEntity.notFound().build();
    }

    // Delete a time slot
    @DeleteMapping("/{timeSlotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long timeSlotId) {
        if (timeSlotService.deleteSlot(timeSlotId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Book a time slot (for students)
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