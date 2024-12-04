package login.permission.project.classes.service;

import java.util.List;
import login.permission.project.classes.model.Permission;
import login.permission.project.classes.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing permissions.
 */
@Service
public class PermissionService {

  @Autowired
  PermissionRepository permissionRepository;

  /**
   * Retrieves all permissions.
   *
   * @return List of all Permission objects
   */
  public List<Permission> getAllPermission() {
    return permissionRepository.findAll();
  }

  /**
   * Creates a new permission.
   *
   * @param permission The Permission object to add
   * @return Success message with permission details
   */
  public String addPermission(Permission permission) {
    permissionRepository.save(permission);
    return String.format("新增權限成功\n權限id: %s\n權限名稱: %s\n權限描述: %s",
            permission.getPermissionId(),
            permission.getPermissionName(),
            permission.getDescription());
  }

  /**
   * Updates an existing permission.
   *
   * @param permission The Permission object with updated info
   * @return Success or failure message
   */
  public String updatePermission(Permission permission) {
    if (permission != null) {
      permissionRepository.save(permission);
      return "修改權限資訊完成";
    } else {
      return "更新權限資訊失敗";
    }
  }

  /**
   * Deletes a permission.
   *
   * @param id The ID of the Permission to delete
   * @return Success message
   */
  public String deletePermission(int id) {
    permissionRepository.deleteById(id);
    return "刪除權限成功";
  }
}