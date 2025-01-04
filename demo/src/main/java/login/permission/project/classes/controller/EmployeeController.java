package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import login.permission.project.classes.model.dto.EmployeeRoleDto;
import login.permission.project.classes.service.EmployeeService;
import login.permission.project.classes.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
    public ResponseEntity<?> updateEmployee (@RequestBody EmployeeRoleDto dto ) {
            return employeeService.updateEmployee(dto);
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token) {
        return employeeService.verifyAccount(token);
    }
}
