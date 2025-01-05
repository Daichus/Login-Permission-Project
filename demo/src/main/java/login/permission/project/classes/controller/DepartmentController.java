package login.permission.project.classes.controller;

import login.permission.project.classes.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import login.permission.project.classes.service.DepartmentService;
import java.util.List;
import login.permission.project.classes.annotation.LogOperation;

/**
 * Depart
 */
@RestController
@RequestMapping("/department/test")
public class DepartmentController {

    @Autowired
    DepartmentService ds;

    @LogOperation(module = "部門管理", operation = "查詢", description = "查詢所有部門")
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('dept_mgt_read')")
    public List<Department> getAllDepartments() {
        return ds.getAllDepartments();
    }

    @LogOperation(module = "部門管理", operation = "新增", description = "新增部門")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('dept_mgt_create')")
    public String addDepartment(@RequestBody Department department) {
        return ds.addDepartment(department);
    }


    @LogOperation(module = "部門管理", operation = "修改", description = "修改部門")
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('dept_mgt_update')")
    public String updateDepartment (@RequestBody Department department) {
        return ds.updateDepartment(department);
    }


    @LogOperation(module = "部門管理", operation = "刪除", description = "刪除部門")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('dept_mgt_delete')")
    public String deleteDepartment(@PathVariable int id) {
        return ds.deleteDepartment(id);
    }

}
