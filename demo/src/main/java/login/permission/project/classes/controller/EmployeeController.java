package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import login.permission.project.classes.service.EmployeeService;
import login.permission.project.classes.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee/test")

public class EmployeeController {

    @Autowired
    EmployeeService es;

    @Autowired
    LoginRecordService lrs;

    @GetMapping("/get")
    public List<Employee> getAllEmployees() {
        return es.getAllEmployees();
    }


    @PostMapping("/login")
    @CrossOrigin("http://localhost:5173/")
    public ResponseEntity<Map<String, String>> login (@RequestBody EmployeeLoginRequest request) {

        String token = es.login(request);
        Map<String, String> response = new HashMap<>();
        response.put("JWT_Token", token);
        System.out.println("token send:\n" +token);

        return  ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
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
}
