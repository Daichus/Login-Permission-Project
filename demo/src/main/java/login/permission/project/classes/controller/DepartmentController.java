package login.permission.project.classes.controller;

import login.permission.project.classes.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import login.permission.project.classes.service.DepartmentService;
import java.util.List;

@RestController
@RequestMapping("/department/test")
public class DepartmentController {

    @Autowired
    DepartmentService ds;

    @GetMapping("/")
    public String home() {
        return "test";
    }

    @GetMapping("/get")
    @ResponseBody
    public List<Department> getAllDepartments() {
        return ds.getAllDepartments();
    }

    @PostMapping("/add")
    public String addDepartment(@RequestBody Department department) {
        return ds.addDepartment(department);
    }
}
