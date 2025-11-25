package com.appdevg4.CitMedConnect.service;

import com.appdevg4.CitMedConnect.entity.TimeSlot;
import com.appdevg4.CitMedConnect.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    // Create
    public TimeSlot createSlot(TimeSlot slot) {
        return timeSlotRepository.save(slot);
    }

    // Read All
    public List<TimeSlot> getAllSlots() {
        return timeSlotRepository.findAll();
    }

    // Read by ID
    public TimeSlot getSlotById(Long id) {
        return timeSlotRepository.findById(id).orElse(null);
    }

    // Update
    public TimeSlot updateSlot(Long id, TimeSlot slot) {
        TimeSlot existing = timeSlotRepository.findById(id).orElse(null);
        
        if (existing != null) {
            existing.setSlotDate(slot.getSlotDate());
            existing.setStartTime(slot.getStartTime());
            existing.setEndTime(slot.getEndTime());
            existing.setAvailable(slot.isAvailable());
            existing.setMaxBookings(slot.getMaxBookings());
            existing.setCurrentBookings(slot.getCurrentBookings());
            return timeSlotRepository.save(existing);
        }
        
        return null;
    }

    // Delete
    public boolean deleteSlot(Long id) {
        if (timeSlotRepository.existsById(id)) {
            timeSlotRepository.deleteById(id);
            return true;
        }
        return false;
    }
}