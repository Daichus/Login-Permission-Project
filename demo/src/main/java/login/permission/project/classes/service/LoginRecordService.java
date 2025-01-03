package login.permission.project.classes.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.model.util.ResponseUtil;
import login.permission.project.classes.repository.LoginRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoginRecordService {

    @Autowired
    LoginRecordRepository loginRecordRepository;

    @Autowired
    JwtService js;

    @Autowired
    JwtUtil jwtUtil;

    public List<LoginRecord> getAllLoginRecords() {
        return loginRecordRepository.findAll();
    }

    public String addLoginRecord(LoginRecord loginRecord) {
        loginRecordRepository.save(loginRecord);
        return String.format("新增登入記錄成功\n登入代號: %s\n員工代號: %s\n登入時間: %s\n登出時間: %s\nip位址: %s\n狀態: %s",
                loginRecord.getRecord_id(),
                loginRecord.getEmployee_id(),
                loginRecord.getLogin_time(),
                loginRecord.getLogout_time(),
                loginRecord.getIp_address(),
                loginRecord.getStatus());
    }

    //登入代號	員工代號	登入時間	登出時間	ip位址	狀態(成功或失敗)

    public String updateLoginRecord (LoginRecord empPositionMap) {
        if(empPositionMap != null) {
            loginRecordRepository.save(empPositionMap);
            return "修改登入記錄資訊完成";
        } else {
            return "更新登入記錄資訊失敗";
        }
    }

    public String deleteLoginRecord (int id) {
        loginRecordRepository.deleteById(id);
        return "刪除登入記錄成功";
    }

    public ResponseEntity<?> updateLogoutTime (HttpServletRequest request) {
        Claims claims = js.isTokenValid(request);

        if(claims != null) {
            Integer loginRecordId = claims.get("loginRecordId", Integer.class);
            Optional<LoginRecord> loginRecordOp = loginRecordRepository.findById(loginRecordId);

            if (loginRecordOp.isPresent()) {
                LoginRecord loginRecord = loginRecordOp.get();
                loginRecord.setLogout_time(LocalDateTime.now());  // 設置登出時間
                loginRecordRepository.save(loginRecord);
            }
            return ResponseEntity.ok("登出成功");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未授權的JWT");
        }
    }

    public  ResponseEntity<?> getLoginRecordById(int employee_id ) {
            List<LoginRecord> loginRecords = loginRecordRepository.findByEmployeeId(employee_id);
            if(!loginRecords.isEmpty()) {
                return ResponseUtil.success("獲取登錄紀錄成功",loginRecords);
            } else {
                return ResponseUtil.error("找不到登錄紀錄",HttpStatus.NOT_FOUND);
            }
        }




}
