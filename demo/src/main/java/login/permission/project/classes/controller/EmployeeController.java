package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import login.permission.project.classes.model.EmployeeManageResponse;
import login.permission.project.classes.service.EmployeeService;
import login.permission.project.classes.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * EmployeeController.
 */
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
  public ResponseEntity<?> login(@RequestBody EmployeeLoginRequest request) {
    return  es.login(request);
  }

  @PostMapping("/add")
  public String addEmployee(@RequestBody Employee employee) {
    return es.addEmployee(employee);
  }

  @PutMapping("/edit")
  public String updateDepartment(@RequestBody Employee employee) {
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

  @GetMapping("/getAllEmployeeInfo")
  public List<EmployeeManageResponse> findAllEmployeeManageResponses() {
    return es.findAllEmployeeManageResponses();
  }
}
