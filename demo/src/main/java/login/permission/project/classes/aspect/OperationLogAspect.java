package login.permission.project.classes.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import login.permission.project.classes.annotation.LogOperation;
import login.permission.project.classes.model.OperationLog;
import login.permission.project.classes.service.OperationLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {
    
    @Autowired
    private OperationLogService operationLogService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Around("@annotation(logOperation)")
    public Object around(ProceedingJoinPoint point, LogOperation logOperation) throws Throwable {
        OperationLog operationLog = new OperationLog();
        
        // 設置基本資訊
        operationLog.setOperationTime(LocalDateTime.now());
        operationLog.setModule(logOperation.module());
        operationLog.setOperationType(logOperation.operation());
        operationLog.setDescription(logOperation.description());
        
        // 獲取當前登入用戶資訊
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            operationLog.setUserId(authentication.getName());  // 假設用戶名就是userId
            operationLog.setUsername(authentication.getName());
        }
        
        // 獲取請求IP
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        operationLog.setIpAddress(getIpAddress(request));
        
        // 獲取請求參數作為beforeValue
        Object[] args = point.getArgs();
        if (args != null && args.length > 0) {
            try {
                operationLog.setBeforeValue(objectMapper.writeValueAsString(args[0]));
            } catch (Exception e) {
                operationLog.setBeforeValue("無法序列化的請求參數");
            }
        }
        
        Object result;
        try {
            result = point.proceed();
            operationLog.setOperationResult("成功");
            try {
                String resultStr = objectMapper.writeValueAsString(result);
                // 如果結果太長，只保留部分內容
                if (resultStr.length() > 2000) {  // 設定一個合理的長度限制
                    resultStr = resultStr.substring(0, 2000) + "... (內容已截斷)";
                }
                operationLog.setAfterValue(resultStr);
            } catch (Exception e) {
                operationLog.setAfterValue("無法序列化的返回結果");
            }
            return result;
        } catch (Exception e) {
            operationLog.setOperationResult("失敗");
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 2000) {
                errorMsg = errorMsg.substring(0, 10000) + "... (錯誤訊息已截斷)";
            }
            operationLog.setAfterValue("操作異常：" + errorMsg);
            throw e;
        } finally {
            saveLog(operationLog);
        }
    }
    
    @Async
    protected void saveLog(OperationLog operationLog) {
        operationLogService.save(operationLog);
    }
    
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}