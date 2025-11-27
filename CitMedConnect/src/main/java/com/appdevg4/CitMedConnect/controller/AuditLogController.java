package com.appdevg4.CitMedConnect.controller;

import com.appdevg4.CitMedConnect.entity.AuditLog;
import com.appdevg4.CitMedConnect.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auditlogs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping
    public AuditLog createLog(@RequestBody AuditLog auditLog) {
        return auditLogService.createLog(auditLog);
    }

    @GetMapping
    public List<AuditLog> getAllLogs() {
        return auditLogService.getAllLogs();
    }

    @GetMapping("/{logId}")
    public ResponseEntity<AuditLog> getLogById(@PathVariable Long logId) {
        AuditLog log = auditLogService.getLogById(logId);
        
        if (log != null) {
            return ResponseEntity.ok(log);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{logId}")
    public ResponseEntity<AuditLog> updateLog(@PathVariable Long logId, @RequestBody AuditLog auditLog) {
        AuditLog updated = auditLogService.updateLog(logId, auditLog);
        
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long logId) {
        if (auditLogService.deleteLog(logId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}