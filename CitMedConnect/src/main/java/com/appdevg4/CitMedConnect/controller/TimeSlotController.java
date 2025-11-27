package com.appdevg4.CitMedConnect.controller;

import com.appdevg4.CitMedConnect.entity.TimeSlot;
import com.appdevg4.CitMedConnect.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    @PostMapping
    public TimeSlot createSlot(@RequestBody TimeSlot slot) {
        return timeSlotService.createSlot(slot);
    }

    @GetMapping
    public List<TimeSlot> getAllSlots() {
        return timeSlotService.getAllSlots();
    }

    @GetMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlot> getSlotById(@PathVariable Long timeSlotId) {
        TimeSlot slot = timeSlotService.getSlotById(timeSlotId);
        
        if (slot != null) {
            return ResponseEntity.ok(slot);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{timeSlotId}")
    public ResponseEntity<TimeSlot> updateSlot(@PathVariable Long timeSlotId, @RequestBody TimeSlot slot) {
        TimeSlot updated = timeSlotService.updateSlot(timeSlotId, slot);
        
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{timeSlotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long timeSlotId) {
        if (timeSlotService.deleteSlot(timeSlotId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}