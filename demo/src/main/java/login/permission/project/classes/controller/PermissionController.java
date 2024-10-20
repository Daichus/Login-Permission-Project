package login.permission.project.classes.controller;

import login.permission.project.classes.model.Permission;
import login.permission.project.classes.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/Permission/test")
public class PermissionController {

  @Autowired
  PermissionService permissionService;

  @GetMapping("/get")
  public List<Permission> getAllPermission() {
    return permissionService.getAllPermission();
  }

  @PostMapping("/add")
  public String addPermission(@RequestBody Permission permission) {
    return permissionService.addPermission(permission);
  }

  @PutMapping("/edit")
  public String updatePermission(@RequestBody Permission permission) {
    return permissionService.updatePermission(permission);
  }

  @DeleteMapping("/delete/{id}")
  public String deletePermission(@PathVariable int id) {
    return permissionService.deletePermission(id);
  }

}
