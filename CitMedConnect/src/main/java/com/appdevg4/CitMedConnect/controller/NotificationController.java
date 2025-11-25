package com.appdevg4.CitMedConnect.controller;

import com.appdevg4.CitMedConnect.entity.NotificationEntity;
import com.appdevg4.CitMedConnect.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/")
    public ResponseEntity<NotificationEntity> createNotification(@RequestBody NotificationEntity notification) {
        try {
            NotificationEntity created = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<NotificationEntity>> getAllNotifications() {
        List<NotificationEntity> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationEntity> getNotificationById(@PathVariable String id) {
        try {
            NotificationEntity notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{schoolId}")
    public ResponseEntity<List<NotificationEntity>> getNotificationsBySchoolId(@PathVariable String schoolId) {
        List<NotificationEntity> notifications = notificationService.getNotificationsByUserId(schoolId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationEntity> updateNotification(
            @PathVariable String id,
            @RequestBody NotificationEntity notification) {
        try {
            NotificationEntity updated = notificationService.updateNotification(id, notification);
        return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable String id) {
        boolean deleted = notificationService.deleteNotification(id);
        if (deleted) {
        return ResponseEntity.ok("Notification deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found");
    }
}