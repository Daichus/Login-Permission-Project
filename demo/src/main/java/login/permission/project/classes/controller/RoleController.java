package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.dto.RoleDTO;
import login.permission.project.classes.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import login.permission.project.classes.annotation.LogOperation;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @LogOperation(module = "角色管理", operation = "查詢", description = "查詢所有角色")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRole() {
        return roleService.getAllRole();
    }

    @LogOperation(module = "角色管理", operation = "新增", description = "新增角色")
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('role_mgt_create')")
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDto, HttpServletRequest request) {
        return roleService.createRole(roleDto, request);
    }

    @LogOperation(module = "角色管理", operation = "修改", description = "修改角色")
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('role_mgt_update')")
    public ResponseEntity<?> updateRole(@RequestBody RoleDTO roleDto, HttpServletRequest request) {
        return roleService.updateRole(roleDto, request);
    }

    @LogOperation(module = "角色管理", operation = "刪除", description = "刪除角色")
    @DeleteMapping("/delete/{role_id}")
    @PreAuthorize("hasAuthority('role_mgt_delete')")
    public ResponseEntity<?> deleteRole(@PathVariable int role_id) {
        return roleService.deleteRole(role_id);
    }

}
