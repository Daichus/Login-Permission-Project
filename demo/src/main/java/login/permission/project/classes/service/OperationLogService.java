package login.permission.project.classes.service;

import login.permission.project.classes.model.OperationLog;
import login.permission.project.classes.model.util.ResponseUtil;
import login.permission.project.classes.repository.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OperationLogService {
    
    @Autowired
    private OperationLogRepository operationLogRepository;
    
    public void save(OperationLog log) {
        operationLogRepository.save(log);
    }
    
    public ResponseEntity<?> getRecentLogs() {
        List<OperationLog> logs = operationLogRepository.findTop100ByOrderByOperationTimeDesc();
        return ResponseUtil.success("查詢成功", logs);
    }
    
    public ResponseEntity<?> searchLogsWithPagination(
            String userId, 
            String module, 
            String operationType, 
            LocalDateTime startTime, 
            LocalDateTime endTime,
            int page,
            int size) {
        // 驗證分頁參數
        if (page < 0) {
            page = 0;
        }
        if (size <= 0 || size > 100) {
            size = 10;
        }
        
        // 建議加入時間範圍的合理性驗證
        if (startTime != null && endTime != null) {
            if (startTime.isAfter(endTime)) {
                return ResponseUtil.error("起始時間不能晚於結束時間", HttpStatus.BAD_REQUEST);
            }
            if (startTime.isAfter(LocalDateTime.now())) {
                return ResponseUtil.error("起始時間不能晚於當前時間", HttpStatus.BAD_REQUEST);
            }
        }
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("operationTime").descending());
        Page<OperationLog> logPage = operationLogRepository.findByConditions(
            userId, operationType, module, startTime, endTime, pageRequest);
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", logPage.getContent());
        response.put("currentPage", logPage.getNumber());
        response.put("totalItems", logPage.getTotalElements());
        response.put("totalPages", logPage.getTotalPages());
        
        return ResponseUtil.success("查詢成功", response);
    }
} 