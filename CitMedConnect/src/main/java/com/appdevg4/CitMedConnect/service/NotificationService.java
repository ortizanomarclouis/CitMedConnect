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
    
    // Create 
    public NotificationEntity createNotification(NotificationEntity notification) {
        if (notification == null) {
            throw new IllegalArgumentException("Notification data cannot be null");
        }
        
        if (notification.getSchool_Id() == null || notification.getSchool_Id().isEmpty()) {
            throw new IllegalArgumentException("User information is required");
        }
        
        // Verify the user exist using school_id
        String schoolId = notification.getSchool_Id();
        userRepository.findById(schoolId)
            .orElseThrow(() -> new RuntimeException("User not found with school_id: " + schoolId));
        
        // Set the verified user and timestamp
        notification.setSchool_Id(schoolId);
        notification.setCreated_At(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }
    
    // Read All
    public List<NotificationEntity> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    // Read By notification ID
    public NotificationEntity getNotificationById(String id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }
    
    // Read By User's School ID
    public List<NotificationEntity> getNotificationsByUserId(String schoolId) {
        return notificationRepository.findBySchool_Id(schoolId);
    }
    
    // Update
    public NotificationEntity updateNotification(String id, NotificationEntity notification) {
        NotificationEntity existing = getNotificationById(id);
        
        if (notification.getTitle() != null && !notification.getTitle().isEmpty()) {
            existing.setTitle(notification.getTitle());
        }
        
        if (notification.getMessage() != null && !notification.getMessage().isEmpty()) {
            existing.setMessage(notification.getMessage());
        }
        
        if (notification.getSchool_Id() != null && !notification.getSchool_Id().isEmpty()) {
            String schoolId = notification.getSchool_Id();
            userRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("User not found with school_id: " + schoolId));
            existing.setSchool_Id(schoolId);
        }
        
        return notificationRepository.save(existing);
    }
    
    // Delete 
    public boolean deleteNotification(String id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
