package login.permission.project.classes.service;


import jakarta.servlet.http.HttpServletRequest;


import login.permission.project.classes.model.*;
import login.permission.project.classes.model.dto.RoleDTO;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.model.util.ResponseUtil;
import login.permission.project.classes.repository.PermissionRepository;
import login.permission.project.classes.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    JwtUtil jwtUtil;


    public ResponseEntity<?> getAllRole() {
        List<Role> roles =  roleRepository.findAll();
        return ResponseUtil.success("獲取所有角色成功", roles);
    }


    /**
     *創建新角色的方法,前端必須傳一個包含jwt token的HttpServletRequest,
     * 和一個包含結構為{
     *     role_id:"1",
     *     roleName:"Admin",
     *     permission_id:["1","2","3"]
     * }資料的body,這筆資料需要用於創建新的Role
     */
    public ResponseEntity<?> createRole (RoleDTO roleDto, HttpServletRequest request) {
        Role newRole = new Role();
        Integer maxRoleId = roleRepository.findMaxRoleId();
        int newRoleId = (maxRoleId == null ? 1 : maxRoleId + 1);
        newRole.setRole_id(newRoleId); // 生成主鍵
        newRole.setRole(roleDto.getRoleName());
        newRole.setPermissions(getPermissionsFromIds(roleDto.getPermission_id()));
        roleRepository.save(newRole);
        return ResponseUtil.success("新增角色成功", HttpStatus.CREATED);

    }


    /**
     *  此方法用於更新角色, 接受一個包含jwt token的HttpServletRequest,
     *  與要用於更新角色的相關資料做為參數,同樣需要前端傳送一個
     *  結構為{
     *        role_id:"1",
     *        roleName:"Admin",
     *        permission_id:["1","2","3"]
     *        }
     *      的資料
     */
    public ResponseEntity<?> updateRole(RoleDTO roleDto, HttpServletRequest request) {
        Optional<Role> roleOption = roleRepository.findById(roleDto.getRole_id());
        if (roleOption.isPresent()) {
            Role role = roleOption.get();
            setupRole(role, roleDto); // 僅更新名稱和權限
            roleRepository.save(role);
            return ResponseUtil.success("修改角色成功", HttpStatus.OK);
        } else {
            return ResponseUtil.error("找不到指定id的角色", HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<?> deleteRole (int role_id){
        Optional<Role> roleOption = roleRepository.findById(role_id);
        if(roleOption.isPresent()) {
            roleRepository.deleteById(role_id);
            return ResponseUtil.success("刪除角色成功", HttpStatus.OK);
        } else {
            return ResponseUtil.error("找不到指定id的角色", HttpStatus.NOT_FOUND);
        }
    }
    /**
     *  更新Role(角色)的方法,設定Role名稱與Role的多對多關聯
     */
    private void setupRole(Role role, RoleDTO roleDto) {
        role.setRole(roleDto.getRoleName());
        role.setPermissions(getPermissionsFromIds(roleDto.getPermission_id()));
    }




    /**
     *  設定Role多對多關聯時需要傳入一個Set<Permission>
     *  此方法根據用戶傳來的權限id, 查找並回傳相應的權限set,屬於setupRole方法的一部分
     */
    private Set<Permission> getPermissionsFromIds(String[] permissionIdsString) {
        try{
            Set<Integer> permissionIds = Arrays.stream(permissionIdsString)
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
            List<Permission> permissions = permissionRepository.findAllById(permissionIds);
            return new HashSet<>(permissions);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("權限 ID 格式錯誤,必須為阿拉伯數字", e);
        }
    }









}
