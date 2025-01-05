package login.permission.project.classes.controller;

import login.permission.project.classes.annotation.LogOperation;
import login.permission.project.classes.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/logs")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @LogOperation(module = "操作日誌", operation = "查詢", description = "查詢前100筆紀錄")
    @GetMapping
    @PreAuthorize("hasAuthority('rec_audit_log_read')")
    public ResponseEntity<?> getRecentLogs() {
        return operationLogService.getRecentLogs();
    }

    @LogOperation(module = "操作日誌", operation = "查詢", description = "根據條件查詢日誌")
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('rec_audit_log_read')")
    public ResponseEntity<?> searchLogsWithPagination(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return operationLogService.searchLogsWithPagination(userId, module, operationType, startTime, endTime, page, size);
    }
}