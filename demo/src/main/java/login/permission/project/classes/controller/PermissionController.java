package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
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

  @GetMapping("/get")
  public ResponseEntity<?> getAllPermission(HttpServletRequest request) {
    return permissionService.getAllPermission(request);
  }

  @PostMapping("/add")
  public ResponseEntity<?> addPermission(@RequestBody Permission permission,HttpServletRequest request) {
    return permissionService.addPermission(permission,request);
  }

  @PutMapping("/edit")
  public ResponseEntity<?> updatePermission(@RequestBody Permission permission,HttpServletRequest  request) {
    return permissionService.updatePermission(permission,request);
  }

  @DeleteMapping("/delete/{id}")
  public String deletePermission(@PathVariable int id) {
    return permissionService.deletePermission(id);
  }

}
