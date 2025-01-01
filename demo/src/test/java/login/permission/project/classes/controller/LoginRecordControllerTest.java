package login.permission.project.classes.controller;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.service.LoginRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginRecordControllerTest {

  private MockMvc mockMvc;

  @InjectMocks
  private LoginRecordController loginRecordController;

  @Mock
  private LoginRecordService loginRecordService;

  @BeforeEach
  void setUp() {
    // 使用 MockMvc 構建 Controller
    mockMvc = MockMvcBuilders.standaloneSetup(loginRecordController).build();

    // 模擬安全上下文
    SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
                    new User("testUser", "password", List.of(new SimpleGrantedAuthority("login_rec_read"))),
                    "password"
            )
    );
  }

  /**
   * 測試：根據權限碼獲取登入記錄
   */
  @Test
  void testGetRecordByPermissionCode() throws Exception {
    // Arrange
    ResponseEntity<String> mockResponse = ResponseEntity.ok("權限驗證成功");

    when(loginRecordService.getRecordByPermissionCode(any(HttpServletRequest.class)))
            .thenAnswer(invocation -> mockResponse); // 使用 thenAnswer 解決泛型問題

    // Act & Assert
    mockMvc.perform(post("/api/loginRecord/getLoginRecord"))
            .andExpect(status().isOk())
            .andExpect(content().string("權限驗證成功"));

    verify(loginRecordService, times(1)).getRecordByPermissionCode(any(HttpServletRequest.class));
  }
}
