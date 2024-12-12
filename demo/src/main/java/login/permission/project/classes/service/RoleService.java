package login.permission.project.classes.service;


import io.jsonwebtoken.Claims;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;


import login.permission.project.classes.model.Permission;
import login.permission.project.classes.model.Role;
import login.permission.project.classes.model.RoleDTO;
import login.permission.project.classes.model.ServerResponse;
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
    JwtService jwtService;

    @Autowired
    PermissionRepository permissionRepository;


    public ResponseEntity<?> getAllRole() {
        List<Role> roles =  roleRepository.findAll();
        return createResponse("獲取所有角色成功",roles, HttpStatus.OK);
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
        Claims claims = jwtService.isTokenValid(request);
        ResponseEntity<?> response;

        if(claims != null) {
            Role role = new Role();
            setupRole(role, roleDto);

            return createResponse("新增角色成功",null, HttpStatus.OK);

        } else {
            return createResponse("你沒有新增角色的權限", null, HttpStatus.UNAUTHORIZED);
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
        Claims claims = jwtService.isTokenValid(request);
        ResponseEntity<?> response;

        if(claims != null) {
            Optional<Role> roleOption = roleRepository.findById(roleDto.getRole_id());
            if(roleOption.isPresent()) {
                Role role = roleOption.get();
                setupRole(role, roleDto);
                return createResponse("修改角色成功",null, HttpStatus.OK);
            } else {
                return createResponse("找不到指定的角色",null, HttpStatus.NOT_FOUND);
            }
        } else {
            return createResponse("你沒有修改角色的權限",null, HttpStatus.UNAUTHORIZED);
        }

    }


    /**
     *  更新role的方法
     */
    private void setupRole(Role role, RoleDTO roleDto) {
        role.setRoleName(roleDto.getRoleName());
        role.setPermissions(getPermissionsFromIds(roleDto.getPermission_id()));
    }

    /**
     *  根據用戶傳來的權限id, 回傳相應的權限set,以供setupRole更新角色的權限資訊
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


    /**
     *  創建回應的方法
     */
    private ResponseEntity<ServerResponse> createResponse(String message, Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(new ServerResponse(message, data));
    }

}
