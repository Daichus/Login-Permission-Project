package login.permission.project.classes.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.Permission;
import login.permission.project.classes.model.Role;
import login.permission.project.classes.model.ServerResponse;
import login.permission.project.classes.model.dto.RoleDTO;
import login.permission.project.classes.security.Config;
import login.permission.project.classes.service.RoleService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RoleControllerTest
 *
 * 此測試類別驗證 RoleController api endpoint 的功能，
 * 包括角色的建立、查詢和更新操作，
 * 以及各種身份驗證和授權場景的測試。
 */
@SpringBootTest
@Import({Config.class})
@ComponentScan(basePackages = "login.permission.project.classes.controller")
@AutoConfigureMockMvc
public class RoleControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private RoleService roleService;

  @MockBean
  private JwtService jwtService;


  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;


  /**
   * 測試成功獲取所有角色（具有正確身份驗證）
   * 預期結果：
   * - 狀態碼：200
   * - 返回：角色列表
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenGetRole_thenStatus200() throws Exception {
    Set<Permission> permissions = new HashSet<>();
    permissions.add(new Permission(1, "READ_USER", "category", "READ_USER"));
    permissions.add(new Permission(2, "WRITE_USER", "category", "WRITE_USER"));

    List<Role> roles = new ArrayList<>();
    roles.add(new Role(1, "ADMIN", permissions));
    roles.add(new Role(2, "USER", new HashSet<>()));

    Claims mockClaims = new DefaultClaims();
    mockClaims.setSubject("admin");
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_read"));
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.getAllRole()).thenReturn(ResponseEntity
            .status(HttpStatus.OK)
            .body(new ServerResponse(
                    "Success",
                    roles
            )));

    mvc.perform(get("/role/getAll")
            .header("Authorization", "Bearer mock-token"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Success"))
            .andExpect(jsonPath("$.data[0].role_id").value(1))
            .andExpect(jsonPath("$.data[0].role").value("ADMIN"))
            .andExpect(jsonPath("$.data[0].permissions").isArray())
            .andExpect(jsonPath("$.data[1].role_id").value(2))
            .andExpect(jsonPath("$.data[1].role").value("USER"))
            .andExpect(jsonPath("$.data[1].permissions").isEmpty());
  }

  /**
   * 測試成功建立角色（具有正確的身份驗證和權限）
   * 預期結果：
   * - 狀態碼：200
   * - 返回：成功訊息
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenCreateRole_thenStatus200() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("NEW_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 1);
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_create"));
    mockClaims.setSubject("admin");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.createRole(any(RoleDTO.class), any(HttpServletRequest.class)))
            .thenReturn(ResponseEntity.ok(new ServerResponse("Success", "")));

    mvc.perform(post("/role/create")
            .header("Authorization", "Bearer mock-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Success"));
  }

  /**
   * 測試在未提供身份驗證令牌的情況下嘗試建立角色
   * 預期結果：
   * - 狀態碼：401 未授權
   * - 返回：錯誤訊息
   */
  @Test
  public void whenCreateRole_TokenNotProvided_ThenStatus401() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("NEW_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(null);

    mvc.perform(post("/role/create")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(roleDto)))
      .andDo(print())
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("Token not provided."));
  }

  /**
   * 測試使用無效的身份驗證令牌嘗試建立角色
   * 預期結果：
   * - 狀態碼：401 未授權
   * - 返回：錯誤訊息
   */
  @Test
  public void whenCreateRole_InvalidToken_ThenStatus401() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("NEW_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    mvc.perform(post("/role/create")
            .header("Authorization", "Bearer invalid-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Invalid token."));
  }

  /**
   * 測試成功更新角色（具有正確的身份驗證和權限）
   * 預期結果：
   * - 狀態碼：200
   * - 返回：成功訊息
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenUpdateRole_thenStatus200() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("UPDATED_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2", "3"});

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 1);
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_update"));
    mockClaims.setSubject("admin");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.updateRole(any(RoleDTO.class), any(HttpServletRequest.class)))
            .thenReturn(ResponseEntity.ok(new ServerResponse("Success", "")));

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Success"));
  }

  /**
   * 測試使用無效的角色ID嘗試更新角色
   * 預期結果：
   * - 狀態碼：400 錯誤請求
   * - 返回：錯誤訊息
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenUpdateRole_withInvalidRoleId_thenStatus400() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(-1);
    roleDto.setRoleName("INVALID_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 1);
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_update"));
    mockClaims.setSubject("admin");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.updateRole(any(RoleDTO.class), any(HttpServletRequest.class)))
            .thenReturn(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ServerResponse("Invalid role ID", null)));

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid role ID"));
  }

  /**
   * 測試在未提供身份驗證令牌的情況下嘗試更新角色
   * 預期結果：
   * - 狀態碼：401 未授權
   * - 返回：錯誤訊息
   */
  @Test
  public void whenUpdateRole_TokenNotProvided_ThenStatus401() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("UPDATED_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(null);

    mvc.perform(put("/role/edit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Token not provided."));
  }

  /**
   * 測試使用無效的身份驗證令牌嘗試更新角色
   * 預期結果：
   * - 狀態碼：401 未授權
   * - 返回：錯誤訊息
   */
  @Test
  public void whenUpdateRole_InvalidToken_ThenStatus401() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("UPDATED_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer invalid-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Invalid token."));
  }

  /**
   * 測試使用權限不足的帳號嘗試更新角色
   * 預期結果：
   * - 狀態碼：403 禁止訪問
   * - 返回：權限不足訊息
   */
  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  public void whenUpdateRole_InsufficientPermissions_ThenStatus403() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("UPDATED_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 2);
    mockClaims.put("permissionCode", Arrays.asList("USER"));
    mockClaims.setSubject("user");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.updateRole(any(RoleDTO.class), any(HttpServletRequest.class)))
            .thenReturn(ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ServerResponse("Insufficient permissions", null)));

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("Insufficient permissions"));
  }

  /**
   * 測試更新角色時使用重複的角色名稱
   * 預期結果：
   * - 狀態碼：400 錯誤請求
   * - 返回：錯誤訊息指出角色名稱已存在
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenUpdateRole_DuplicateRoleName_thenStatus400() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("EXISTING_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 1);
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_update"));
    mockClaims.setSubject("admin");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.updateRole(any(RoleDTO.class), any(HttpServletRequest.class)))
            .thenReturn(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ServerResponse("Role name already exists", null)));

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Role name already exists"));
  }

  /**
   * 測試使用無效的權限ID更新角色
   * 預期結果：
   * - 狀態碼：400 錯誤請求
   * - 返回：錯誤訊息指出無效的權限ID
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenUpdateRole_InvalidPermissionId_thenStatus400() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);
    roleDto.setRoleName("VALID_ROLE");
    roleDto.setPermission_id(new String[]{"999"});

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 1);
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_update"));
    mockClaims.setSubject("admin");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.updateRole(any(RoleDTO.class), any(HttpServletRequest.class)))
            .thenReturn(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ServerResponse("Invalid permission ID", null)));

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid permission ID"));
  }

  /**
   * 測試使用無效的請求體格式更新角色
   * 預期結果：
   * - 狀態碼：400 錯誤請求
   * - 返回：錯誤請求訊息
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenUpdateRole_InvalidRequestBody_thenStatus400() throws Exception {
    String invalidJson = "{\"role_id\": 1, \"roleName\": Invalid JSON}";

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 1);
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_update"));
    mockClaims.setSubject("admin");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  /**
   * 測試更新角色時缺少必填欄位
   * 預期結果：
   * - 狀態碼：400 錯誤請求
   * - 返回：錯誤訊息指出缺少必填欄位
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenUpdateRole_MissingRequiredFields_thenStatus400() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(1);

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 1);
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_update"));
    mockClaims.setSubject("admin");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.updateRole(any(RoleDTO.class), any(HttpServletRequest.class)))
            .thenReturn(ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ServerResponse("Missing required fields", null)));

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Missing required fields"));
  }

  /**
   * 測試嘗試更新不存在的角色
   * 預期結果：
   * - 狀態碼：404 找不到資源
   * - 返回：錯誤訊息指出找不到角色
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenUpdateRole_RoleNotFound_thenStatus404() throws Exception {
    RoleDTO roleDto = new RoleDTO();
    roleDto.setRole_id(999);
    roleDto.setRoleName("NON_EXISTENT_ROLE");
    roleDto.setPermission_id(new String[]{"1", "2"});

    Claims mockClaims = new DefaultClaims();
    mockClaims.put("loginRecordId", 1);
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "role_mgt_update"));
    mockClaims.setSubject("admin");
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(mockClaims);

    when(roleService.updateRole(any(RoleDTO.class), any(HttpServletRequest.class)))
            .thenReturn(ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ServerResponse("Role not found", null)));

    mvc.perform(put("/role/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(roleDto)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Role not found"));
  }


}