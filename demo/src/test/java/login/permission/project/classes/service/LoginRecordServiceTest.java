package login.permission.project.classes.service;

import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.repository.LoginRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LoginRecordServiceTest {

  @InjectMocks
  private LoginRecordService loginRecordService;

  @Mock
  private LoginRecordRepository loginRecordRepository;

  @Mock
  private JwtService jwtService;

  @Mock
  private HttpServletRequest request;

  @BeforeEach
   void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * 測試：取得所有登入記錄
   * 驗證 Service 是否正確調用 Repository 並返回結果。
   */
  @Test
  void getAllLoginRecords() {
    // Arrange
    List<LoginRecord> records = List.of(
            new LoginRecord(1, "127.0.0.1", LocalDateTime.now(), null,"SUCCESS"),
            new LoginRecord(2, "127.0.0.2", LocalDateTime.now(), null,"FAIL")
    );
    when(loginRecordRepository.findAll()).thenReturn(records);

    // Act
    List<LoginRecord> result = loginRecordService.getAllLoginRecords();

    // Assert
    assertEquals(2, result.size());
    assertEquals("127.0.0.1", result.get(0).getIp_address());
    assertEquals("127.0.0.2", result.get(1).getIp_address());
    verify(loginRecordRepository, times(1)).findAll();
  }

  /**
   * 測試：新增登入記錄
   * 驗證記錄成功儲存，並返回正確訊息。
   */
  @Test
  void addLoginRecord() {
    // Arrange
    LoginRecord record = new LoginRecord(
            1,         // employee_id
            "127.0.0.1",          // ip_address
            LocalDateTime.now(), // login_time
            null,                // logout_time
            "SUCCESS"             // status
            );

    when(loginRecordRepository.save(record)).thenReturn(record);

    // Act
    String result = loginRecordService.addLoginRecord(record);

    // Assert
    assertTrue(result.contains("新增登入記錄成功"));
    verify(loginRecordRepository, times(1)).save(record);

  }

  /**
   * 測試：更新登入記錄
   * 驗證記錄成功更新，並返回正確訊息。
   */
  @Test
  void updateLoginRecord() {
    // Arrange
    LoginRecord record = new LoginRecord(
            1,         // employee_id
            "127.0.0.1",          // ip_address
            LocalDateTime.now(), // login_time
            LocalDateTime.now(), // logout_time
            "UPDATED"             // status
    );

    when(loginRecordRepository.save(record)).thenReturn(record);

    // Act
    String result = loginRecordService.updateLoginRecord(record);

    // Assert
    assertEquals("修改登入記錄資訊完成", result);
    verify(loginRecordRepository, times(1)).save(record);
  }

  /**
   * 測試：刪除登入記錄
   * 驗證記錄成功刪除，並返回正確訊息。
   */
  @Test
  void deleteLoginRecord() {
    // Arrange
    int recordId = 1;
    doNothing().when(loginRecordRepository).deleteById(recordId);

    // Act
    String result = loginRecordService.deleteLoginRecord(recordId);

    // Assert
    assertEquals("刪除登入記錄成功", result);
    verify(loginRecordRepository, times(1)).deleteById(recordId);
  }

  /**
   * 測試：更新登出時間（成功情境）
   * 驗證在 JWT 合法時，登出時間被正確更新。
   */
  @Test
  void updateLogoutTime_Success() {
    // Arrange
    Claims claims = mock(Claims.class);
    when(jwtService.isTokenValid(request)).thenReturn(claims);
    when(claims.get("loginRecordId", Integer.class)).thenReturn(1);

    LoginRecord record = new LoginRecord(
            1,
            "127.0.0.1",
            LocalDateTime.now(),
            null,
            "SUCCESS"
    );

    when(loginRecordRepository.findById(1)).thenReturn(Optional.of(record));

    // Act
    ResponseEntity<?> result = loginRecordService.updateLogoutTime(request);

    // Assert
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("登出成功", result.getBody());
    verify(loginRecordRepository, times(1)).save(record);
  }

  /**
   * 測試：根據權限碼獲取登入記錄（擁有正確權限）
   * 驗證使用者擁有 'login_rec_read' 權限時，所有記錄都被返回。
   */
  @Test
  void getRecordByPermissionCode_WithPermission() {
    // Arrange
    Claims claims = mock(Claims.class);
    when(jwtService.isTokenValid(request)).thenReturn(claims);
    when(claims.get("permissionCode", List.class)).thenReturn(List.of("login_rec_read"));

    List<LoginRecord> records = List.of(
            new LoginRecord(1, "127.0.0.1", LocalDateTime.now(), null, "SUCCESS")
    );

    when(loginRecordRepository.findAll()).thenReturn(records);

    // Act
    ResponseEntity<?> result = loginRecordService.getRecordByPermissionCode(request);

    // Assert
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(records, result.getBody());
    verify(loginRecordRepository, times(1)).findAll();
  }

  /**
   * 測試：根據權限碼獲取登入記錄（無權限）
   * 驗證當使用者無相關權限時，只返回個人記錄。
   */
  @Test
  void getRecordByPermissionCode_WithoutPermission() {
    // Arrange
    Claims claims = mock(Claims.class);
    when(jwtService.isTokenValid(request)).thenReturn(claims);
    when(claims.get("permissionCode", List.class)).thenReturn(List.of("other_permission"));
    when(claims.getSubject()).thenReturn("1");

    List<LoginRecord> records = List.of(
            new LoginRecord(1, "127.0.0.1", LocalDateTime.now(), null, "SUCCESS")
    );

    when(loginRecordRepository.findByEmployeeId(1)).thenReturn(records);

    // Act
    ResponseEntity<?> result = loginRecordService.getRecordByPermissionCode(request);

    // Assert
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(records, result.getBody());
    verify(loginRecordRepository, times(1)).findByEmployeeId(1);
  }

}