package com.CitMedConnect.MedConnect.controller;

import com.CitMedConnect.MedConnect.entity.NotificationEntity;
import com.CitMedConnect.MedConnect.service.NotificationService;
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
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<NotificationEntity>> getAllNotifications() {
        List<NotificationEntity> notifications = notificationService.getAllNotifications();
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationEntity> getNotificationById(@PathVariable int id) {
        try {
            NotificationEntity notification = notificationService.getNotificationById(id);
            return new ResponseEntity<>(notification, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{schoolId}")
    public ResponseEntity<List<NotificationEntity>> getNotificationsBySchoolId(@PathVariable int schoolId) {
        List<NotificationEntity> notifications = notificationService.getNotificationsByUserId(schoolId);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationEntity> updateNotification(
            @PathVariable int id,
            @RequestBody NotificationEntity notification) {
        try {
            NotificationEntity updated = notificationService.updateNotification(id, notification);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable int id) {
        boolean deleted = notificationService.deleteNotification(id);
        if (deleted) {
            return new ResponseEntity<>("Notification deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Notification not found", HttpStatus.NOT_FOUND);
    }
}