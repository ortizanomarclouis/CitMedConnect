package com.appdevg5.CitMedConnect.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Notification_Id")
    private String Notification_Id;
    
    @Column(name = "School_Id", nullable = false)
    private String School_Id;
    
    @Column(nullable = false)
    private String Title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String Message;
    
    @Column(name = "Created_At", nullable = false, updatable = false)
    private LocalDateTime Created_At = LocalDateTime.now();
    
    public String getNotification_Id() {
        return Notification_Id;
    }
    
    public void setNotification_Id(String Notification_Id) {
        this.Notification_Id = Notification_Id;
    }
    
    public String getSchool_Id() {
        return School_Id;
    }
    
    public void setSchool_Id(String School_Id) {
        this.School_Id = School_Id;
    }
    
    public String getTitle() {
        return Title;
    }
    
    public void setTitle(String Title) {
        this.Title = Title;
    }
    
    public String getMessage() {
        return Message;
    }
    
    public void setMessage(String Message) {
        this.Message = Message;
    }
    
    public LocalDateTime getCreated_At() {
        return Created_At;
    }
    
    public void setCreated_At(LocalDateTime Created_At) {
        this.Created_At = Created_At;
    }
}