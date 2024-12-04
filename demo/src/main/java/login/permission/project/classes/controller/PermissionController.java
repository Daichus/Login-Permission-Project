package login.permission.project.classes.controller;

import java.util.List;
import login.permission.project.classes.model.Permission;
import login.permission.project.classes.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing permissions.
 */
@RestController
@RequestMapping("/Permission/test")
public class PermissionController {

  @Autowired
  PermissionService permissionService;

  /**
   * Gets all permissions.
   *
   * @return List of all Permission objects
   */
  @GetMapping("/get")
  public List<Permission> getAllPermission() {
    return permissionService.getAllPermission();
  }

  /**
   * Creates a new permission.
   *
   * @param permission The Permission object to add
   * @return String indicating the operation result
   */
  @PostMapping("/add")
  public String addPermission(@RequestBody Permission permission) {
    return permissionService.addPermission(permission);
  }

  /**
   * Updates an existing permission.
   *
   * @param permission The Permission object with updated info
   * @return String indicating the operation result
   */
  @PutMapping("/edit")
  public String updatePermission(@RequestBody Permission permission) {
    return permissionService.updatePermission(permission);
  }

  /**
   * Deletes a permission by ID.
   *
   * @param id The ID of the Permission to delete
   * @return String indicating the operation result
   */
  @DeleteMapping("/delete/{id}")
  public String deletePermission(@PathVariable int id) {
    return permissionService.deletePermission(id);
  }
}