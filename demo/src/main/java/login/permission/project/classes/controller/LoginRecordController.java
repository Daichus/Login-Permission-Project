package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loginRecord")
public class LoginRecordController {

    @Autowired
    private LoginRecordService lrs; // 變數名稱改為 lrs

    // 獲取所有登入記錄
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('login_rec_read')")
    public List<LoginRecord> getAllLoginRecords() {
        return lrs.getAllLoginRecords();
    }

    // 新增登入記錄
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('login_rec_create')")
    public String addLoginRecord(@RequestBody LoginRecord loginRecord) {
        return lrs.addLoginRecord(loginRecord);
    }

    // 修改登入記錄
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('login_rec_update')")
    public String updateLoginRecord(@RequestBody LoginRecord loginRecord) {
        return lrs.updateLoginRecord(loginRecord);
    }

    // 刪除登入記錄
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('login_rec_delete')")
    public String deleteLoginRecord(@PathVariable int id) {
        return lrs.deleteLoginRecord(id);
    }

    @PostMapping("/getLoginRecord")
    @PreAuthorize("hasAuthority('login_rec_create','login_rec_read')")
    public ResponseEntity<?> getRecordByPermissionId(HttpServletRequest request) {
        return lrs.getRecordByPermissionCode(request);
    }
}

