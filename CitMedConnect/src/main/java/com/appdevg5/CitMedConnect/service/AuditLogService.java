package com.appdevg5.CitMedConnect.service;

import com.appdevg5.CitMedConnect.entity.AuditLog;
import com.appdevg5.CitMedConnect.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // Create
    public AuditLog createLog(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    // Read All
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    // Read by ID
    public AuditLog getLogById(Long id) {
        return auditLogRepository.findById(id).orElse(null);
    }

    // Update
    public AuditLog updateLog(Long id, AuditLog auditLog) {
        AuditLog existing = auditLogRepository.findById(id).orElse(null);
        
        if (existing != null) {
            existing.setSchoolId(auditLog.getSchoolId());
            existing.setActionType(auditLog.getActionType());
            existing.setTableName(auditLog.getTableName());
            existing.setRecordId(auditLog.getRecordId());
            existing.setCreatedBy(auditLog.getCreatedBy());
            existing.setCreatedAt(auditLog.getCreatedAt());
            return auditLogRepository.save(existing);
        }
        
        return null;
    }


    public boolean deleteLog(Long id) {
        if (auditLogRepository.existsById(id)) {
            auditLogRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
