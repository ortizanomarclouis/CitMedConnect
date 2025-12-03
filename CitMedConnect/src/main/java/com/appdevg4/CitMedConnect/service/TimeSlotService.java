package com.appdevg4.CitMedConnect.service;

import com.appdevg4.CitMedConnect.entity.TimeSlot;
import com.appdevg4.CitMedConnect.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public TimeSlot createSlot(TimeSlot slot) {
        return timeSlotRepository.save(slot);
    }

    public List<TimeSlot> getAllSlots() {
        return timeSlotRepository.findAll();
    }

    public TimeSlot getSlotById(Long id) {
        return timeSlotRepository.findById(id).orElse(null);
    }

    public TimeSlot updateSlot(Long id, TimeSlot slot) {
        TimeSlot existing = timeSlotRepository.findById(id).orElse(null);
        
        if (existing != null) {
            existing.setSlotDate(slot.getSlotDate());
            existing.setStartTime(slot.getStartTime());
            existing.setEndTime(slot.getEndTime());
            existing.setAvailable(slot.isAvailable());
            existing.setMaxBookings(slot.getMaxBookings());
            existing.setCurrentBookings(slot.getCurrentBookings());
            existing.setStaffId(slot.getStaffId());
            existing.setWithinBusinessHours(slot.isWithinBusinessHours());
            return timeSlotRepository.save(existing);
        }
        
        return null;
    }

    public boolean deleteSlot(Long id) {
        if (timeSlotRepository.existsById(id)) {
            timeSlotRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Find available time slots for a specific date
    public List<TimeSlot> findAvailableSlots(LocalDate date) {
        return timeSlotRepository.findBySlotDateAndIsAvailable(date, true);
    }
    
    // Get time slots for a specific staff member
    public List<TimeSlot> getStaffSlots(String staffId) {
        return timeSlotRepository.findByStaffId(staffId);
    }
}