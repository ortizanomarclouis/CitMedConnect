package com.appdevg4.CitMedConnect.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.appdevg4.CitMedConnect.entity.NotificationEntity;
import com.appdevg4.CitMedConnect.entity.UserEntity;
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
        
        if (notification.getNotificationType() == null || notification.getNotificationType().isEmpty()) {
            throw new IllegalArgumentException("Notification type cannot be null or empty");
        }
        
        if (notification.getSchoolId() == null || notification.getSchoolId().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        
        String schoolId = notification.getSchoolId();
        userRepository.findById(schoolId)
            .orElseThrow(() -> new RuntimeException("User not found with school_id: " + schoolId));
        
        notification.setSchoolId(schoolId);
        notification.setCreatedAt(LocalDateTime.now());
        
        if (notification.getIsGlobal() == null) {
            notification.setIsGlobal(false);
        }
        
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
        
        if (notification.getNotificationType() != null && !notification.getNotificationType().isEmpty()) {
            existing.setNotificationType(notification.getNotificationType());
        }
        
        if (notification.getIsGlobal() != null) {
            existing.setIsGlobal(notification.getIsGlobal());
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
    
    private String getCurrentUserSchoolId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            String username = authentication.getName();
            // Try to find user by email first
            try {
                UserEntity user = userRepository.findByEmail(username);
                if (user != null) {
                    return user.getSchoolId();
                }
                // If not found by email, try by schoolId directly
                user = userRepository.findBySchoolId(username);
                if (user != null) {
                    return user.getSchoolId();
                }
                // Finally, try by ID (schoolId as primary key)
                user = userRepository.findById(username).orElse(null);
                if (user != null) {
                    return user.getSchoolId();
                }
            } catch (Exception e) {
                // Log error but continue
                System.err.println("Error finding current user: " + e.getMessage());
            }
        }
        return null;
    }
    
    public NotificationEntity markNotificationAsRead(String id) {
        NotificationEntity notification = getNotificationById(id);
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }
    
    public List<NotificationEntity> sendNotificationToAllStudents(String title, String message, String type) {
        List<UserEntity> students = userRepository.findByRole("STUDENT");
        
        if (students == null || students.isEmpty()) {
            throw new RuntimeException("No students found in the system. Cannot send broadcast notification.");
        }
        
        List<NotificationEntity> notifications = new ArrayList<>();
        String currentUserSchoolId = getCurrentUserSchoolId();
        
        System.out.println("DEBUG: Current user schoolId: " + currentUserSchoolId);
        System.out.println("DEBUG: Total students found: " + students.size());
        System.out.println("DEBUG: Notification type: " + type);
        
        for (UserEntity student : students) {
            // DON'T send to yourself!
            if (currentUserSchoolId == null || !student.getSchoolId().equals(currentUserSchoolId)) {
                NotificationEntity notification = new NotificationEntity();
                notification.setNotificationId(UUID.randomUUID().toString());
                notification.setSchoolId(student.getSchoolId());
                notification.setTitle(title);
                notification.setMessage(message);
                notification.setNotificationType(type); // <-- THIS IS THE KEY LINE!
                notification.setIsGlobal(false);
                notification.setCreatedAt(LocalDateTime.now());
                
                notifications.add(notificationRepository.save(notification));
                System.out.println("DEBUG: Sent notification to: " + student.getSchoolId() + " with type: " + type);
            } else {
                System.out.println("DEBUG: SKIPPED notification to: " + student.getSchoolId() + " (current user)");
            }
        }
        
        System.out.println("DEBUG: Total notifications sent: " + notifications.size());
        return notifications;
    }
    
    public List<NotificationEntity> sendNotificationToEveryone(String title, String message, String type) {
        List<UserEntity> allUsers = userRepository.findAll();
        
        if (allUsers == null || allUsers.isEmpty()) {
            throw new RuntimeException("No users found in the system. Cannot send broadcast notification.");
        }
        
        List<NotificationEntity> notifications = new ArrayList<>();
        String currentUserSchoolId = getCurrentUserSchoolId();
        
        System.out.println("DEBUG: Current user schoolId: " + currentUserSchoolId);
        System.out.println("DEBUG: Total users found: " + allUsers.size());
        System.out.println("DEBUG: Notification type: " + type);
        
        for (UserEntity user : allUsers) {
            // DON'T send to yourself!
            if (currentUserSchoolId == null || !user.getSchoolId().equals(currentUserSchoolId)) {
                NotificationEntity notification = new NotificationEntity();
                notification.setNotificationId(UUID.randomUUID().toString());
                notification.setSchoolId(user.getSchoolId());
                notification.setTitle(title);
                notification.setMessage(message);
                notification.setNotificationType(type); // <-- THIS IS THE KEY LINE!
                notification.setIsGlobal(true);
                notification.setCreatedAt(LocalDateTime.now());
                
                notifications.add(notificationRepository.save(notification));
                System.out.println("DEBUG: Sent notification to: " + user.getSchoolId() + " with type: " + type);
            } else {
                System.out.println("DEBUG: SKIPPED notification to: " + user.getSchoolId() + " (current user)");
            }
        }
        
        System.out.println("DEBUG: Total notifications sent: " + notifications.size());
        return notifications;
    }
    
    public List<NotificationEntity> getNotificationsForUser(String schoolId, String userRole) {
        List<NotificationEntity> userSpecificNotifications = notificationRepository.findBySchoolId(schoolId);
        List<NotificationEntity> studentBroadcasts = notificationRepository.findByNotificationType("STUDENT_BROADCAST");
        List<NotificationEntity> globalBroadcasts = notificationRepository.findByNotificationType("GLOBAL_BROADCAST");
        
        List<NotificationEntity> allNotifications = new ArrayList<>();
        allNotifications.addAll(userSpecificNotifications);
        
        if ("STUDENT".equals(userRole)) {
            allNotifications.addAll(studentBroadcasts);
        }
        
        allNotifications.addAll(globalBroadcasts);
        
        return allNotifications.stream()
            .sorted((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()))
            .collect(Collectors.toList());
    }
    
    public NotificationEntity sendNotificationToUser(String recipientId, String title, String message, String type) {
        // Verify the user exists
        userRepository.findById(recipientId)
            .orElseThrow(() -> new RuntimeException("User not found with school_id: " + recipientId));
        
        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationId(UUID.randomUUID().toString());
        notification.setSchoolId(recipientId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setNotificationType(type);
        notification.setIsGlobal(false);
        notification.setCreatedAt(LocalDateTime.now());
        
        NotificationEntity saved = notificationRepository.save(notification);
        System.out.println("Notification sent to user: " + recipientId + " with type: " + type);
        
        return saved;
    }
}
