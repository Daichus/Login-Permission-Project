package login.permission.project.classes.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.repository.LoginRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoginRecordService {

  @Autowired
  LoginRecordRepository lrr;

  @Autowired
  JwtService js;

  public List<LoginRecord> getAllLoginRecords() {
    return lrr.findAll();
  }

  public String addLoginRecord(LoginRecord loginRecord) {
    lrr.save(loginRecord);
    return String.format("新增登入記錄成功\n登入代號: %s\n員工代號: %s\n登入時間: %s\n登出時間: %s\nip位址: %s\n狀態: %s",
                loginRecord.getRecord_id(),
                loginRecord.getEmployee_id(),
                loginRecord.getLogin_time(),
                loginRecord.getLogout_time(),
                loginRecord.getIp_address(),
                loginRecord.getStatus());
  }


  public String updateLoginRecord(LoginRecord empPositionMap) {
    if (empPositionMap != null) {
      lrr.save(empPositionMap);
      return "修改登入記錄資訊完成";
    } else {
      return "更新登入記錄資訊失敗";
    }
  }

  public String deleteLoginRecord(int id) {
    lrr.deleteById(id);
    return "刪除登入記錄成功";
  }

  public ResponseEntity<?> updateLogoutTime(HttpServletRequest request) {
    Claims claims = js.isTokenValid(request);

    if (claims != null) {
      Integer loginRecordId = claims.get("loginRecordId", Integer.class);
      Optional<LoginRecord> loginRecordOp = lrr.findById(loginRecordId);

      if (loginRecordOp.isPresent()) {
        LoginRecord loginRecord = loginRecordOp.get();
        loginRecord.setLogout_time(LocalDateTime.now());  // 設置登出時間
        lrr.save(loginRecord);
      }
      return ResponseEntity.ok("登出成功");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未授權的JWT");
    }
  }

  public  ResponseEntity<?> getRecordByPermissionCode(HttpServletRequest request) {
    Claims claims = js.isTokenValid(request);
    List<LoginRecord> loginRecords = null;

    if (claims != null) {
      List<String> permissionCode = claims.get("permissionCode", List.class);
      for (String code : permissionCode) {
        if (code.equals("login_rec_read") || code.equals("login_rec_update") || code.equals("login_rec_create") || code.equals("login_rec_delete")) {
          loginRecords = lrr.findAll();
        } else {
          loginRecords = lrr.findByEmployeeId(Integer.parseInt(claims.getSubject()));
        }
      }
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login record not found");
    }
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(loginRecords);

  }

}
