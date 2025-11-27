package com.appdevg4.CitMedConnect.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appdevg4.CitMedConnect.entity.NotificationEntity;
import com.appdevg4.CitMedConnect.repository.NotificationRepository;
import com.appdevg4.CitMedConnect.repository.UserRepository;


@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public NotificationEntity createNotification(NotificationEntity notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification data cannot be null");
        }
        
        if (notification.getSchoolId() == null || notification.getSchoolId().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        
        String schoolId = notification.getSchoolId();
        userRepository.findById(schoolId)
            .orElseThrow(() -> new RuntimeException("User not found with school_id: " + schoolId));
        
        notification.setSchoolId(schoolId);
        notification.setCreatedAt(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }
    
    public List<NotificationEntity> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    public NotificationEntity getNotificationById(String id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }
    
    public List<NotificationEntity> getNotificationsByUserId(String schoolId) {
        return notificationRepository.findBySchoolId(schoolId);
    }
    
    public NotificationEntity updateNotification(String id, NotificationEntity notification) {
        NotificationEntity existing = getNotificationById(id);
        
        if (notification.getTitle() != null && !notification.getTitle().isEmpty()) {
            existing.setTitle(notification.getTitle());
        }
        
        if (notification.getMessage() != null && !notification.getMessage().isEmpty()) {
            existing.setMessage(notification.getMessage());
        }
        
        if (notification.getSchoolId() != null && !notification.getSchoolId().isEmpty()) {
            String schoolId = notification.getSchoolId();
            userRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("User not found with school_id: " + schoolId));
            existing.setSchoolId(schoolId);
        }
        
        return notificationRepository.save(existing);
    }
    
    public boolean deleteNotification(String id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
