package com.appdevg5.CitMedConnect.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_slots")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeSlotId;

    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAvailable;
    private int maxBookings;
    private int currentBookings;

   
    public TimeSlot() {}

   
    public Long getTimeSlotId() { return timeSlotId; }
    public void setTimeSlotId(Long timeSlotId) { this.timeSlotId = timeSlotId; }

    public LocalDate getSlotDate() { return slotDate; }
    public void setSlotDate(LocalDate slotDate) { this.slotDate = slotDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public int getMaxBookings() { return maxBookings; }
    public void setMaxBookings(int maxBookings) { this.maxBookings = maxBookings; }

    public int getCurrentBookings() { return currentBookings; }
    public void setCurrentBookings(int currentBookings) { this.currentBookings = currentBookings; }
}
