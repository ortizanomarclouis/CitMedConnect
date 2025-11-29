package com.appdevg4.CitMedConnect.repository;

import com.appdevg4.CitMedConnect.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    UserEntity findByEmail(String email);
    UserEntity findBySchoolId(String schoolId);
    List<UserEntity> findByRole(String role);
}