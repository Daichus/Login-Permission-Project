package login.permission.project.classes.controller;

import java.util.List;
import login.permission.project.classes.model.Department;
import login.permission.project.classes.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * DepartmentController.
 */
@RestController
@RequestMapping("/department/test")
public class DepartmentController {

  @Autowired
  DepartmentService ds;

  @GetMapping("/get")
  public List<Department> getAllDepartments() {
    return ds.getAllDepartments();
  }

  @PostMapping("/add")
  public String addDepartment(@RequestBody Department department) {
    return ds.addDepartment(department);
  }

  @PutMapping("/edit")
  public String updateDepartment(@RequestBody Department department) {
    return ds.updateDepartment(department);
  }


  //GPT推薦方法
  @DeleteMapping("/delete/{id}")
  public String deleteDepartment(@PathVariable int id) {
    return ds.deleteDepartment(id);
  }

}
