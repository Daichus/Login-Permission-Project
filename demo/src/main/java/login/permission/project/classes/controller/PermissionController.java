package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.annotation.LogOperation;
import login.permission.project.classes.model.Permission;
import login.permission.project.classes.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/Permission/test")
public class PermissionController {

  @Autowired
  PermissionService permissionService;

  @LogOperation(module = "權限管理", operation = "查詢", description = "查詢所有權限")
  @GetMapping("/get")
  public ResponseEntity<?> getAllPermission(HttpServletRequest request) {
    return permissionService.getAllPermission(request);
  }

  @LogOperation(module = "權限管理", operation = "查詢", description = "查詢單一權限")
  @GetMapping("/getById/{id}")
  public ResponseEntity<?> getPermissionById(HttpServletRequest request, @PathVariable int id) {
    return permissionService.getPermissionById(request, id);
  }

  @LogOperation(module = "權限管理", operation = "新增", description = "新增權限")
  @PostMapping("/add")
  public ResponseEntity<?> addPermission(@RequestBody Permission permission,HttpServletRequest request) {
    return permissionService.addPermission(permission,request);
  }

  @LogOperation(module = "權限管理", operation = "修改", description = "修改權限")
  @PutMapping("/edit")
  public ResponseEntity<?> updatePermission(@RequestBody Permission permission,HttpServletRequest  request) {
    return permissionService.updatePermission(permission,request);
  }

  @LogOperation(module = "權限管理", operation = "刪除", description = "刪除權限")
  @DeleteMapping("/delete/{id}")
  public String deletePermission(@PathVariable int id) {
    return permissionService.deletePermission(id);
  }

}
