package login.permission.project.classes.service;

import login.permission.project.classes.model.PermissionMap;
import login.permission.project.classes.repository.PermissionMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionMapService {
    @Autowired
    PermissionMapRepository dr;

    public List<PermissionMap> getAllPermissionMaps() {
        return dr.findAll();
    }

    public String addPermissionMap(PermissionMap permissionMap) {
        dr.save(permissionMap);
        return String.format("新增權限設定成功\n權限設定代號: %s\n職位代號: %s\n權限代號: %s",
                permissionMap.getPermissionMap_id(),
                permissionMap.getPosition_id(),
                permissionMap.getPermission_id());
    }

    public String updatePermissionMap (PermissionMap permissionMap) {
        if(permissionMap != null) {
            dr.save(permissionMap);
            return "修改權限設定資訊完成";
        } else {
            return "更新權限設定資訊失敗";
        }
    }

    public String deletePermissionMap (int id) {
        dr.deleteById(id);
        return "刪除權限設定成功";
    }
}
