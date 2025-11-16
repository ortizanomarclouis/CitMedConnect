package com.CitMedConnect.MedConnect.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CitMedConnect.MedConnect.entity.NotificationEntity;
import com.CitMedConnect.MedConnect.entity.UserEntity;
import com.CitMedConnect.MedConnect.repository.NotificationRepository;
import com.CitMedConnect.MedConnect.repository.UserRepository;


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
        
        if (notification.getUser() == null || notification.getUser().getSchoolId() == 0) {
            throw new IllegalArgumentException("User information is required");
        }
        
        // Verify the user exist using school_id
        int schoolId = notification.getUser().getSchoolId();
        UserEntity user = userRepository.findById(schoolId)
            .orElseThrow(() -> new RuntimeException("User not found with school_id: " + schoolId));
        
        // Set the verified user and timestamp
        notification.setUser(user);
        notification.setCreatedAt(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }
    
    // Read All
    public List<NotificationEntity> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    // Read By notification ID
    public NotificationEntity getNotificationById(int id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }
    
    // Read By User's School ID
    public List<NotificationEntity> getNotificationsByUserId(int schoolId) {
        return notificationRepository.findByUser_SchoolId(schoolId);
    }
    
    // Update
    public NotificationEntity updateNotification(int id, NotificationEntity notification) {
        NotificationEntity existing = getNotificationById(id);
        
        if (notification.getTitle() != null && !notification.getTitle().isEmpty()) {
            existing.setTitle(notification.getTitle());
        }
        
        if (notification.getMessage() != null && !notification.getMessage().isEmpty()) {
            existing.setMessage(notification.getMessage());
        }
        
        if (notification.getUser() != null && notification.getUser().getSchoolId() != 0) {
            int schoolId = notification.getUser().getSchoolId();
            UserEntity user = userRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("User not found with school_id: " + schoolId));
            existing.setUser(user);
        }
        
        return notificationRepository.save(existing);
    }
    
    // Delete 
    public boolean deleteNotification(int id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
