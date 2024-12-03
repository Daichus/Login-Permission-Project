package login.permission.project.classes.service;

import java.util.List;
import login.permission.project.classes.model.PermissionMap;
import login.permission.project.classes.repository.PermissionMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing permission mappings.
 */
@Service
public class PermissionMapService {

  @Autowired
  PermissionMapRepository dr;

  /**
   * Retrieves all permission mappings.
   *
   * @return List of all PermissionMap objects
   */
  public List<PermissionMap> getAllPermissionMaps() {
    return dr.findAll();
  }

  /**
   * Creates a new permission mapping.
   *
   * @param permissionMap The PermissionMap object to add
   * @return Success message with mapping details
   */
  public String addPermissionMap(PermissionMap permissionMap) {
    dr.save(permissionMap);
    return String.format("新增權限設定成功\n權限設定代號: %s\n職位代號: %s\n權限代號: %s",
            permissionMap.getPermissionMapId(),
            permissionMap.getPositionId(),
            permissionMap.getPermissionId());
  }

  /**
   * Updates an existing permission mapping.
   *
   * @param permissionMap The PermissionMap object with updated info
   * @return Success or failure message
   */
  public String updatePermissionMap(PermissionMap permissionMap) {
    if (permissionMap != null) {
      dr.save(permissionMap);
      return "修改權限設定資訊完成";
    } else {
      return "更新權限設定資訊失敗";
    }
  }

  /**
   * Deletes a permission mapping.
   *
   * @param id The ID of the PermissionMap to delete
   * @return Success message
   */
  public String deletePermissionMap(int id) {
    dr.deleteById(id);
    return "刪除權限設定成功";
  }
}