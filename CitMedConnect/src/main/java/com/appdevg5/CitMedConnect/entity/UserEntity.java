package com.appdevg5.CitMedConnect.entity;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "School_Id")
    private String School_Id;
    
    @Column(name = "First_Name", nullable = false)
    private String First_Name;
    
    @Column(name = "Last_Name", nullable = false)
    private String Last_Name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    private String gender;
    
    @Column(nullable = false)
    private int age;
    
    @Column(name = "Created_At", nullable = false, updatable = false)
    private Date Created_At = new Date();
    
    public String getSchool_Id() {
        return School_Id;
    }
    
    public void setSchool_Id(String School_Id) {
        this.School_Id = School_Id;
    }
    
    public String getUserId() {
        return this.School_Id;
    }
    
    public String getFirst_Name() {
        return First_Name;
    }
    
    public void setFirst_Name(String First_Name) {
        this.First_Name = First_Name;
    }
    
    public String getLast_Name() {
        return Last_Name;
    }
    
    public void setLast_Name(String Last_Name) {
        this.Last_Name = Last_Name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public Date getCreated_At() {
        return Created_At;
    }
    
    public void setCreated_At(Date Created_At) {
        this.Created_At = Created_At;
    }
}
