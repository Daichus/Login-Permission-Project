package controller;


import classes.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import service.DepartmentService;

@RestController
@RequestMapping("department/test")

public class DepartmentController {

    @Autowired
    DepartmentService ds;

    @PostMapping("/add")
    public String addDepartment(@RequestBody Department department) {
        return ds.addDepartment(department);
    }
}
