package com.appdevg5.CitMedConnect.repository;

import com.appdevg5.CitMedConnect.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}