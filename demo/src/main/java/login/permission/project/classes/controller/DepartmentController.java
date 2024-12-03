package login.permission.project.classes.controller;

import login.permission.project.classes.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('dept_mgt_read')")
    public List<Department> getAllDepartments() {
        return ds.getAllDepartments();
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('dept_mgt_create')")
    public String addDepartment(@RequestBody Department department) {
        return ds.addDepartment(department);
    }


    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('dept_mgt_update')")
    public String updateDepartment (@RequestBody Department department) {
        return ds.updateDepartment(department);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('dept_mgt_delete')")
    public String deleteDepartment(@PathVariable int id) {
        return ds.deleteDepartment(id);
    }

}
