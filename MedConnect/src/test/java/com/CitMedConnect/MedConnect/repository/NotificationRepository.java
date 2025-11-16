package com.CitMedConnect.MedConnect.repository;

import com.CitMedConnect.MedConnect.entity.NotificationEntity;
import com.CitMedConnect.MedConnect.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findByUser(UserEntity user);
    List<NotificationEntity> findByUser_SchoolId(int schoolId);
}