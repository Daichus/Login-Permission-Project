package login.permission.project.classes.service;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.model.Permission;
import login.permission.project.classes.model.util.ResponseUtil;
import login.permission.project.classes.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

  @Autowired
  PermissionRepository permissionRepository;

  @Autowired
  JwtUtil jwtUtil;


  /**
   * 前端透過傳遞jwt token驗證,
   * 獲取所有權限,回傳的資料結構為[ {permission_id, permission_name,category, permission_code},{}...]
   */
  public ResponseEntity<?> getAllPermission(HttpServletRequest request) {
    try{
      jwtUtil.validateRequest(request);
      List<Permission> permissions = permissionRepository.findAll();
      return ResponseUtil.success("獲取權限成功",permissions);
    } catch(IllegalArgumentException e) {
      return ResponseUtil.error("你沒有獲取所有權限的權力", HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * 添加Permission的方法,前端需傳遞jwt token供驗證外,需傳遞和Permission物件相同構造的body,但無須傳送id,
   * 例:{
   *   permission_name:"Admin",
   *   category:"部門管理",
   *   permission_code:"dept_read_edit"
   * }
   */
  public ResponseEntity<?> addPermission(Permission permission, HttpServletRequest request) {
    try{
      jwtUtil.validateRequest(request);
      permissionRepository.save(permission);
      return ResponseUtil.success("新增權限成功",permission);
    } catch (IllegalArgumentException e) {
      return ResponseUtil.error("你沒有添加權限的權利",HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   *更新權限的代碼,方法大致與添加類似,只是前端傳遞的資料須包含permission id,結構必須為:
   *  {
   *     permission_id: 1
   *     permission_name:"Admin",
   *     category:"部門管理",
   *     permission_code:"dept_read_edit"
   *  }
   */
  public ResponseEntity<?> updatePermission(Permission permission, HttpServletRequest request) {
    try {
      jwtUtil.validateRequest(request);
      Optional<Permission> oldPermission = permissionRepository.findById(permission.getPermission_id());
      if (oldPermission.isPresent()) {
        permissionRepository.save(permission);
        return ResponseUtil.success("更新權限成功", permission);
      } else {
        return ResponseUtil.error("找不到指定id的權限", HttpStatus.NOT_FOUND);
      }
    } catch(IllegalArgumentException e) {
      return ResponseUtil.error("你沒有修改權限的權利",HttpStatus.UNAUTHORIZED);
    }
  }

  //delete
  public String deletePermission(int id) {
    permissionRepository.deleteById(id);
    return "刪除權限成功";
  }


}
