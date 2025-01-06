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


@RestController
@RequestMapping("/employee/test")

public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    LoginRecordService lrs;

    @GetMapping("/get")
    public ResponseEntity<?> getAllEmployees(HttpServletRequest request) {
        return employeeService.getAllEmployees( request);
    }

    @PostMapping("/login")
    @CrossOrigin("http://localhost:5173/")
    public ResponseEntity<?> login (@RequestBody EmployeeLoginRequest request) {
        return  employeeService.login(request);
    }

    @PostMapping("/getEmployeeById/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id,HttpServletRequest request) {
        return employeeService.getEmployeeById(id,request);
    }

    @PostMapping("/add")
    public String addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @PutMapping("/edit")
    public String updateDepartment (@RequestBody Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        return employeeService.deleteEmployee(id);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return lrs.updateLogoutTime(request);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEmployee (@RequestBody EmployeeUpdateDto dto ) {
            return employeeService.updateEmployee(dto);
    }

    // 註冊驗證
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token) {
        return employeeService.verifyAccount(token);
    }

    // 忘記密碼，寄Email
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        return employeeService.requestPasswordReset(email);
    }

    // 密碼重置驗證
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
