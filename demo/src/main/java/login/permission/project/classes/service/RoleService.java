package login.permission.project.classes.service;


import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;


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
        return ResponseUtil.success("獲取所有角色成功",  HttpStatus.OK);
    }


    /**
     *創建新角色的方法,前端必須傳一個包含jwt token的HttpServletRequest,
     * 和一個包含結構為{
     *     role_id:"1",
     *     roleName:"Admin",
     *     permission_id:["1","2","3"]
     * }資料的body,這筆資料需要用於創建新的Role
     */
    public ResponseEntity<?> createNewRole (RoleDTO roleDto, HttpServletRequest request) {
        try {
            jwtUtil.validateRequest(request);
            Role role = new Role();
            setupRole(role, roleDto);
            roleRepository.save(role);
            return  ResponseUtil.success("新增角色成功",  HttpStatus.OK);
        } catch ( IllegalArgumentException e) {
            return  ResponseUtil.error("你沒有新增角色的權限",  HttpStatus.UNAUTHORIZED);
        }

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
        try{
            jwtUtil.validateRequest(request);
            Optional<Role> roleOption = roleRepository.findById(roleDto.getRole_id());
            if(roleOption.isPresent()) {
                Role role = roleOption.get();
                setupRole(role, roleDto);
                roleRepository.save(role);
                return ResponseUtil.success("修改角色成功",  HttpStatus.OK);
            } else {
                return   ResponseUtil.error("找不到指定id的角色",  HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return   ResponseUtil.error("你沒有修改角色的權限",  HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     *  更新Role(角色)的方法,設定Role名稱與Role的多對多關聯
     */
    private void setupRole(Role role, RoleDTO roleDto) {
        role.setRoleName(roleDto.getRoleName());
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
