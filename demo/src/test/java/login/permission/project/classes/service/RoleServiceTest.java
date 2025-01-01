package login.permission.project.classes.service;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.Permission;
import login.permission.project.classes.model.Role;
import login.permission.project.classes.model.ServerResponse;
import login.permission.project.classes.model.dto.RoleDTO;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.repository.PermissionRepository;
import login.permission.project.classes.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * RoleService 的單元測試類
 */
public class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private PermissionRepository permissionRepository;

  @Mock
  private JwtUtil jwtUtil;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  private RoleService roleService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * 測試獲取所有角色
   */
  @Test
  void whenGetAllRole_thenSuccess() {
    // 準備測試資料
    List<Role> mockRoles = Arrays.asList(
            new Role(1, "ADMIN", new HashSet<>()),
            new Role(2, "USER", new HashSet<>())
    );

    when(roleRepository.findAll()).thenReturn(mockRoles);

    // 執行測試
    ResponseEntity<ServerResponse> response = roleService.getAllRole();

    // 驗證結果
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("查詢所有角色成功", response.getBody().getMessage());
    assertEquals(2, ((List<Role>)response.getBody().getData()).size());
    verify(roleRepository, times(1)).findAll();
  }

  /**
   * 測試成功創建角色
   */
  @Test
  void whenCreateRole_thenSuccess() {
    // 準備測試資料
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRoleName("TEST_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    Permission permission1 = new Permission(1, "PERM1", "category", "desc");
    Permission permission2 = new Permission(2, "PERM2", "category", "desc");
    List<Permission> permissions = Arrays.asList(permission1, permission2);

    // Mock 所需的行為
    doNothing().when(jwtUtil).validateRequest(any(HttpServletRequest.class));
    when(permissionRepository.findAllById(any())).thenReturn(permissions);
    when(roleRepository.save(any(Role.class))).thenReturn(new Role());

    // 執行測試
    ResponseEntity<ServerResponse> response = roleService.createRole(roleDto, request);

    // 驗證結果
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("新增角色成功", response.getBody().getMessage());
    verify(roleRepository, times(1)).save(any(Role.class));
  }

  /**
   * 測試創建角色時無權限的情況
   */
  @Test
  void whenCreateRole_withoutPermission_thenFail() {
    // 準備測試資料
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRoleName("TEST_ROLE");

    // Mock 拋出異常的情況
    doThrow(new IllegalArgumentException()).when(jwtUtil).validateRequest(any(HttpServletRequest.class));

    // 執行測試
    ResponseEntity<ServerResponse> response = roleService.createRole(roleDto, request);

    // 驗證結果
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("你沒有新增角色的權限", response.getBody().getMessage());
    verify(roleRepository, never()).save(any(Role.class));
  }

  /**
   * 測試成功更新角色
   */
  @Test
  void whenUpdateRole_thenSuccess() {
    // 準備測試資料
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("UPDATED_ROLE");
    roleDto.setPermission_id(new String[]{"1"});

    Role existingRole = new Role(1, "OLD_ROLE", new HashSet<>());
    Permission permission = new Permission(1, "PERM1", "category", "desc");

    // Mock 所需的行為
    doNothing().when(jwtUtil).validateRequest(any(HttpServletRequest.class));
    when(roleRepository.findById(1)).thenReturn(Optional.of(existingRole));
    when(permissionRepository.findAllById(any())).thenReturn(Collections.singletonList(permission));
    when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

    // 執行測試
    ResponseEntity<ServerResponse> response = roleService.updateRole(roleDto, request);

    // 驗證結果
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("修改角色成功", response.getBody().getMessage());
    verify(roleRepository, times(1)).save(any(Role.class));
  }

  /**
   * 測試更新不存在的角色
   */
  @Test
  void whenUpdateRole_roleNotFound_thenFail() {
    // 準備測試資料
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(999);
    roleDto.setRoleName("NON_EXISTENT_ROLE");

    // Mock 所需的行為
    doNothing().when(jwtUtil).validateRequest(any(HttpServletRequest.class));
    when(roleRepository.findById(999)).thenReturn(Optional.empty());

    // 執行測試
    ResponseEntity<ServerResponse> response = roleService.updateRole(roleDto, request);

    // 驗證結果
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("找不到指定id的角色", response.getBody().getMessage());
    verify(roleRepository, never()).save(any(Role.class));
  }

  /**
   * 測試更新角色時無權限的情況
   */
  @Test
  void whenUpdateRole_withoutPermission_thenFail() {
    // 準備測試資料
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("TEST_ROLE");

    // Mock 拋出異常的情況
    doThrow(new IllegalArgumentException()).when(jwtUtil).validateRequest(any(HttpServletRequest.class));

    // 執行測試
    ResponseEntity<ServerResponse> response = roleService.updateRole(roleDto, request);

    // 驗證結果
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("你沒有修改角色的權限", response.getBody().getMessage());
    verify(roleRepository, never()).save(any(Role.class));
  }

  /**
   * 測試創建角色時權限ID格式錯誤
   */
  @Test
  void whenCreateRole_withInvalidPermissionId_thenFail() {
    // 準備測試資料
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRoleName("TEST_ROLE");
    roleDto.setPermission_id(new String[]{"invalid_id"}); // 非數字的ID

    // Mock 所需的行為
    doNothing().when(jwtUtil).validateRequest(any(HttpServletRequest.class));

    // 執行測試
    ResponseEntity<ServerResponse> response = roleService.createRole(roleDto, request);

    // 驗證結果
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    verify(roleRepository, never()).save(any(Role.class));
  }
}