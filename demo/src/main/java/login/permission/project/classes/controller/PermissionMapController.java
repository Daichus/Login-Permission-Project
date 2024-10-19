package login.permission.project.classes.controller;

import login.permission.project.classes.model.PermissionMap;
import login.permission.project.classes.service.PermissionMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissionMap")
public class PermissionMapController {

    @Autowired
    private PermissionMapService pms; // 使用 pms 作為變數名稱

    // 獲取所有權限設定
    @GetMapping("/get")
    public List<PermissionMap> getAllPermissionMaps() {
        return pms.getAllPermissionMaps();
    }

    // 新增權限設定
    @PostMapping("/add")
    public String addPermissionMap(@RequestBody PermissionMap permissionMap) {
        return pms.addPermissionMap(permissionMap);
    }

    // 修改權限設定
    @PutMapping("/edit")
    public String editPermissionMap(@RequestBody PermissionMap permissionMap) {
        return pms.updatePermissionMap(permissionMap);
    }

    // 刪除權限設定
    @DeleteMapping("/delete/{id}")
    public String deletePermissionMap(@PathVariable int id) {
        return pms.deletePermissionMap(id);
    }
}
