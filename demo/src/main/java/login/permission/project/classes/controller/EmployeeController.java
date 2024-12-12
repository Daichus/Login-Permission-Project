package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import login.permission.project.classes.service.EmployeeService;
import login.permission.project.classes.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/employee/test")

public class EmployeeController {

    @Autowired
    EmployeeService es;

    @Autowired
    LoginRecordService lrs;

    @GetMapping("/get")
    public ResponseEntity<?> getAllEmployees(HttpServletRequest request) {
        return es.getAllEmployees( request);
    }

    @PostMapping("/login")
    @CrossOrigin("http://localhost:5173/")
    public ResponseEntity<?> login (@RequestBody EmployeeLoginRequest request) {
        return  es.login(request);
    }

    @PostMapping("/getEmployeeById/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable int id,HttpServletRequest request) {
        return es.getEmployeeById(id,request);
    }

    @PostMapping("/add")
    public String addEmployee(@RequestBody Employee employee) {
        return es.addEmployee(employee);
    }

    @PutMapping("/edit")
    public String updateDepartment (@RequestBody Employee employee) {
        return es.updateEmployee(employee);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        return es.deleteEmployee(id);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return lrs.updateLogoutTime(request);
    }



    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token) {
        return es.verifyAccount(token);
    }
}
