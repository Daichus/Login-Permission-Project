package login.permission.project.classes.controller;

import login.permission.project.classes.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import login.permission.project.classes.service.DepartmentService;
import java.util.List;

/**
 * Depart
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
    public String updateDepartment (@RequestBody Department department) {
        return ds.updateDepartment(department);
    }

//    @DeleteMapping("/delete")
//    public  String deleteDepartment (@RequestBody Department department) {
//        return ds.deleteDepartment(department.getDepartment_id());
//    }

    //GPT推薦方法
    @DeleteMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable int id) {
        return ds.deleteDepartment(id);
    }

}
