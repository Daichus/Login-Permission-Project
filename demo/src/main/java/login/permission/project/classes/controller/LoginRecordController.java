package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loginRecord")
public class LoginRecordController {

    @Autowired
    private LoginRecordService loginRecordService; // 變數名稱改為 lrs

    // 獲取所有登入記錄
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('login_rec_read')")
    public List<LoginRecord> getAllLoginRecords() {
        return loginRecordService.getAllLoginRecords();
    }

    // 新增登入記錄
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('login_rec_create')")
    public String addLoginRecord(@RequestBody LoginRecord loginRecord) {
        return loginRecordService.addLoginRecord(loginRecord);
    }

    // 修改登入記錄
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('login_rec_update')")
    public String updateLoginRecord(@RequestBody LoginRecord loginRecord) {
        return loginRecordService.updateLoginRecord(loginRecord);
    }

    // 刪除登入記錄
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('login_rec_delete')")
    public String deleteLoginRecord(@PathVariable int id) {
        return loginRecordService.deleteLoginRecord(id);
    }

    @GetMapping ("/getLoginRecord/{employee_id}")
    @PreAuthorize("hasAuthority('rec_login_read_self')")
    public ResponseEntity<?> getRecordByPermissionId(@PathVariable int employee_id) {
        return loginRecordService.getLoginRecordById(employee_id);
    }
}

