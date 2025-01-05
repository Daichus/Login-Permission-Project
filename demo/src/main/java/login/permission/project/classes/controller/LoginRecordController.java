package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import login.permission.project.classes.annotation.LogOperation;

import java.util.List;

@RestController
@RequestMapping("/api/loginRecord")
public class LoginRecordController {

    @Autowired
    private LoginRecordService loginRecordService; // 變數名稱改為 lrs

    @LogOperation(module = "登入記錄", operation = "查詢", description = "查詢所有登入記錄")
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('login_rec_read')")
    public List<LoginRecord> getAllLoginRecords() {
        return loginRecordService.getAllLoginRecords();
    }

    @LogOperation(module = "登入記錄", operation = "新增", description = "新增登入記錄")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('login_rec_create')")
    public String addLoginRecord(@RequestBody LoginRecord loginRecord) {
        return loginRecordService.addLoginRecord(loginRecord);
    }

    @LogOperation(module = "登入記錄", operation = "修改", description = "修改登入記錄")
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('login_rec_update')")
    public String updateLoginRecord(@RequestBody LoginRecord loginRecord) {
        return loginRecordService.updateLoginRecord(loginRecord);
    }

    @LogOperation(module = "登入記錄", operation = "刪除", description = "刪除登入記錄")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('login_rec_delete')")
    public String deleteLoginRecord(@PathVariable int id) {
        return loginRecordService.deleteLoginRecord(id);
    }

    @LogOperation(module = "登入記錄", operation = "查詢", description = "查詢個人登入記錄")
    @GetMapping("/getLoginRecord/{employee_id}")
    @PreAuthorize("hasAuthority('rec_login_read_self')")
    public ResponseEntity<?> getRecordByPermissionId(@PathVariable int employee_id) {
        return loginRecordService.getLoginRecordById(employee_id);
    }
}

