package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import login.permission.project.classes.model.dto.EmployeeUpdateDto;
import login.permission.project.classes.model.util.ResponseUtil;
import login.permission.project.classes.service.EmployeeService;
import login.permission.project.classes.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import login.permission.project.classes.annotation.LogOperation;

@RestController
@RequestMapping("/employee/test")

public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    LoginRecordService lrs;

    @LogOperation(module = "員工管理", operation = "查詢", description = "查詢所有員工")
    @GetMapping("/get")
    public ResponseEntity<?> getAllEmployees(HttpServletRequest request) {
        return employeeService.getAllEmployees( request);
    }

    @LogOperation(module = "員工管理", operation = "登入", description = "員工登入")
    @PostMapping("/login")
    @CrossOrigin("http://localhost:5173/")
    public ResponseEntity<?> login (@RequestBody EmployeeLoginRequest request) {
        return  employeeService.login(request);
    }

    @LogOperation(module = "員工管理", operation = "查詢", description = "根據ID查詢員工")
    @PostMapping("/getEmployeeById/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id,HttpServletRequest request) {
        return employeeService.getEmployeeById(id,request);
    }

    @LogOperation(module = "員工管理", operation = "新增", description = "新增員工")
    @PostMapping("/add")
    public String addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @LogOperation(module = "員工管理", operation = "修改", description = "修改員工資料")
    @PutMapping("/edit")
    public String updateDepartment (@RequestBody Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    @LogOperation(module = "員工管理", operation = "刪除", description = "刪除員工")
    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        return employeeService.deleteEmployee(id);
    }

    @LogOperation(module = "員工管理", operation = "登出", description = "員工登出")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return lrs.updateLogoutTime(request);
    }

    @LogOperation(module = "員工管理", operation = "修改", description = "更新員工角色")
    @PutMapping("/update")
    public ResponseEntity<?> updateEmployee (@RequestBody EmployeeUpdateDto dto ) {
            return employeeService.updateEmployee(dto);
    }

    @LogOperation(module = "員工管理", operation = "驗證", description = "驗證員工帳號")
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token) {
        return employeeService.verifyAccount(token);
    }

    @LogOperation(module = "員工管理", operation = "重置密碼", description = "申請重置密碼")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        return employeeService.requestPasswordReset(email);
    }

    @LogOperation(module = "員工管理", operation = "重置密碼", description = "執行重置密碼")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            String newPassword = payload.get("newPassword");

            if (token == null || newPassword == null) {
                return ResponseUtil.error("缺少必要參數", HttpStatus.BAD_REQUEST);
            }

            return employeeService.resetPassword(token, newPassword);
        } catch (Exception e) {
            return ResponseUtil.error("重置密碼時發生錯誤: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
