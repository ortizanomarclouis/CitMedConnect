package com.appdevg4.CitMedConnect.repository;

import com.appdevg4.CitMedConnect.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
    List<NotificationEntity> findBySchool_Id(String schoolId);
}