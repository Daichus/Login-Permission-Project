package login.permission.project.classes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.service.LoginRecordService;
import login.permission.project.classes.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * LoginRecordControllerTest
 *
 * 測試 LoginRecordController 的各種 API 端點，涵蓋正常操作、異常處理及權限驗證等場景。
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import({login.permission.project.classes.security.Config.class})
public class LoginRecordControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private LoginRecordService loginRecordService;

  @MockBean
  private JwtService jwtService;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * 測試：獲取所有登入記錄（具備正確權限）
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenGetAllLoginRecords_thenStatus200() throws Exception {
    List<LoginRecord> records = List.of(
            new LoginRecord(1, "127.0.0.1", LocalDateTime.now(), null, "SUCCESS"),
            new LoginRecord(2, "127.0.0.2", LocalDateTime.now(), null, "FAIL")
    );

    when(loginRecordService.getAllLoginRecords()).thenReturn(records);

    mvc.perform(get("/api/loginRecord/get")
                    .header("Authorization", "Bearer mock-token"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].employee_id").value(1))
            .andExpect(jsonPath("$[1].employee_id").value(2));
  }

  /**
   * 測試：新增登入記錄（具備正確權限）
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenAddLoginRecord_thenStatus200() throws Exception {
    LoginRecord loginRecord = new LoginRecord(1, "127.0.0.1", LocalDateTime.now(), null, "SUCCESS");

    when(loginRecordService.addLoginRecord(any(LoginRecord.class)))
            .thenReturn("新增登入記錄成功");

    mvc.perform(post("/api/loginRecord/add")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRecord)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("新增登入記錄成功"));
  }

  /**
   * 測試：修改登入記錄（具備正確權限）
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenUpdateLoginRecord_thenStatus200() throws Exception {
    LoginRecord loginRecord = new LoginRecord(1, "127.0.0.1", LocalDateTime.now(), LocalDateTime.now(), "UPDATED");

    when(loginRecordService.updateLoginRecord(any(LoginRecord.class)))
            .thenReturn("修改登入記錄資訊完成");

    mvc.perform(put("/api/loginRecord/edit")
                    .header("Authorization", "Bearer mock-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRecord)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("修改登入記錄資訊完成"));
  }

  /**
   * 測試：刪除登入記錄（具備正確權限）
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenDeleteLoginRecord_thenStatus200() throws Exception {
    when(loginRecordService.deleteLoginRecord(1))
            .thenReturn("刪除登入記錄成功");

    mvc.perform(delete("/api/loginRecord/delete/1")
                    .header("Authorization", "Bearer mock-token"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("刪除登入記錄成功"));
  }

  /**
   * 測試：基於權限碼獲取登入記錄（具備正確權限）
   */
  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenGetRecordByPermissionCode_thenStatus200() throws Exception {
    Claims claims = new DefaultClaims();
    claims.put("permissionCode", Arrays.asList("login_rec_read"));

    when(jwtService.verifyToken(any(HttpServletRequest.class))).thenReturn(claims);

    ResponseEntity<String> response = ResponseEntity.ok("權限驗證成功");

    // 使用 doReturn 來避免泛型問題

    doReturn(response).when(loginRecordService).getRecordByPermissionCode(any(HttpServletRequest.class));
    // Act & Assert
    mvc.perform(post("/api/loginRecord/getLoginRecord")
                    .header("Authorization", "Bearer mock-token"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("權限驗證成功"));
  }

  /**
   * 測試：未授權訪問 - Token 未提供
   */
  @Test
  public void whenGetAllLoginRecordsWithoutToken_thenStatus401() throws Exception {
    mvc.perform(get("/api/loginRecord/get"))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Unauthorized: Full authentication is required to access this resource"));
  }

//  /**
//   * 測試：使用無效 Token 訪問
//   */
//  @Test
//  public void whenGetAllLoginRecordsWithInvalidToken_thenStatus401() throws Exception {
//    when(jwtService.isTokenValid(any(HttpServletRequest.class))).thenReturn(null);
//
//    mvc.perform(get("/api/loginRecord/get")
//                    .header("Authorization", "Bearer invalid-token"))
//            .andDo(print())
//            .andExpect(status().isUnauthorized())
//            .andExpect(content().string("Unauthorized: Invalid Token"));
//
//  }

}
