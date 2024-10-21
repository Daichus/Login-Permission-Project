package login.permission.project.classes.service;

import login.permission.project.classes.model.Permission;
import login.permission.project.classes.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

  @Autowired
  PermissionRepository permissionRepository;

  //get
  public List<Permission> getAllPermission() {
    return permissionRepository.findAll();
  }

  //post
  public String addPermission(Permission permission) {
    permissionRepository.save(permission);
    return String.format("新增權限成功\n權限id: %s\n權限名稱: %s\n權限描述: %s",
            permission.getPermission_id(),
            permission.getPermission_name(),
            permission.getDescription());
  }

  //put
  public String updatePermission(Permission permission) {
    if(permission != null) {
      permissionRepository.save(permission);
      return "修改權限資訊完成";
    } else {
      return "更新權限資訊失敗";
    }
  }

  //delete
  public String deletePermission(int id) {
    permissionRepository.deleteById(id);
    return "刪除權限成功";
  }


}
