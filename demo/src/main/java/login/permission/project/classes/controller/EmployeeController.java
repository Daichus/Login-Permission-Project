package login.permission.project.classes.controller;



import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import login.permission.project.classes.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee/test")
public class EmployeeController {

    @Autowired
    EmployeeService es;

    @GetMapping("/get")
    public List<Employee> getAllEmployees() {
        return es.getAllEmployees();
    }

    @GetMapping("/login")
    public Employee login (@RequestBody EmployeeLoginRequest request) {
        return  es.login(request);
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

}
