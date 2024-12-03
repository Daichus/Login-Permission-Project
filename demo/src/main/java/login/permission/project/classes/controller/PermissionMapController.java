package login.permission.project.classes.controller;

import java.util.List;
import login.permission.project.classes.model.PermissionMap;
import login.permission.project.classes.service.PermissionMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing permission mappings between positions and permissions.
 */
@RestController
@RequestMapping("/api/permissionMap")
public class PermissionMapController {

  @Autowired
  private PermissionMapService pms;

  /**
   * Retrieves all permission mappings in the system.
   *
   * @return List of all PermissionMap objects
   */
  @GetMapping("/get")
  public List<PermissionMap> getAllPermissionMaps() {
    return pms.getAllPermissionMaps();
  }

  /**
   * Creates a new permission mapping.
   *
   * @param permissionMap The PermissionMap object to be added
   * @return String indicating the result of the operation
   */
  @PostMapping("/add")
  public String addPermissionMap(@RequestBody PermissionMap permissionMap) {
    return pms.addPermissionMap(permissionMap);
  }

  /**
   * Updates an existing permission mapping.
   *
   * @param permissionMap The PermissionMap object with updated information
   * @return String indicating the result of the operation
   */
  @PutMapping("/edit")
  public String editPermissionMap(@RequestBody PermissionMap permissionMap) {
    return pms.updatePermissionMap(permissionMap);
  }

  /**
   * Deletes a permission mapping by its ID.
   *
   * @param id The ID of the PermissionMap to be deleted
   * @return String indicating the result of the operation
   */
  @DeleteMapping("/delete/{id}")
  public String deletePermissionMap(@PathVariable int id) {
    return pms.deletePermissionMap(id);
  }
}