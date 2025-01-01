package login.permission.project.classes.model;

import login.permission.project.classes.repository.LoginRecordRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LoginRecordModelTest {

  @Autowired
  private LoginRecordRepository loginRecordRepository;

  private LoginRecord sampleRecord;

  @BeforeEach
  void setUp() {
    sampleRecord = new LoginRecord();
    sampleRecord.setEmployee_id(1);
    sampleRecord.setIp_address("127.0.0.1");
    sampleRecord.setLogin_time(LocalDateTime.now());
    sampleRecord.setLogout_time(null);
    sampleRecord.setStatus("SUCCESS");

    sampleRecord = loginRecordRepository.save(sampleRecord);
  }

  /**
   * 測試：成功創建登入記錄
   * 驗證記錄成功保存並返回有效 ID。
   */
  @Test
  void createLoginRecord_Success() {
    // Arrange
    LoginRecord record = new LoginRecord();
    record.setEmployee_id(2);
    record.setIp_address("192.168.1.1");
    record.setLogin_time(LocalDateTime.now());
    record.setLogout_time(null);
    record.setStatus("SUCCESS");

    // Act
    LoginRecord savedRecord = loginRecordRepository.save(record);

    // Assert
    assertNotNull(savedRecord.getRecord_id());
    assertEquals(2, savedRecord.getEmployee_id());
    assertEquals("192.168.1.1", savedRecord.getIp_address());
  }

  /**
   * 測試：使用有效 ID 查詢登入記錄
   */
  @Test
  void readLoginRecordById_ValidId() {
    // Act
    Optional<LoginRecord> result = loginRecordRepository.findById(sampleRecord.getRecord_id());

    // Assert
    assertTrue(result.isPresent());
    assertEquals(sampleRecord.getRecord_id(), result.get().getRecord_id());
  }

  /**
   * 測試：使用無效 ID 查詢登入記錄
   */
  @Test
  void readLoginRecordById_InvalidId() {
    // Act
    Optional<LoginRecord> result = loginRecordRepository.findById(999);

    // Assert
    assertFalse(result.isPresent());
  }

  /**
   * 測試：成功更新登入記錄
   */
  @Test
  void updateLoginRecord_Success() {
    // Arrange
    sampleRecord.setStatus("UPDATED");
    sampleRecord.setLogout_time(LocalDateTime.now());

    // Act
    LoginRecord updatedRecord = loginRecordRepository.save(sampleRecord);

    // Assert
    assertEquals("UPDATED", updatedRecord.getStatus());
    assertNotNull(updatedRecord.getLogout_time());
  }

  /**
   * 測試：成功刪除登入記錄
   */
  @Test
  void deleteLoginRecord_Success() {
    // Act
    loginRecordRepository.deleteById(sampleRecord.getRecord_id());
    Optional<LoginRecord> result = loginRecordRepository.findById(sampleRecord.getRecord_id());

    // Assert
    assertFalse(result.isPresent());
  }

  /**
   * 測試：驗證 Employee 關聯映射
   */
  @Test
  void validateEmployeeRelation() {
    // Act
    Optional<LoginRecord> result = loginRecordRepository.findById(sampleRecord.getRecord_id());

    // Assert
    assertTrue(result.isPresent());
    assertNotNull(result.get().getEmployee());
  }

  /**
   * 測試：驗證欄位映射和約束
   */
  @Test
  void validateFieldConstraints() {
    // Arrange
    LoginRecord record = new LoginRecord();
    record.setEmployee_id(3);
    record.setIp_address("10.0.0.1");
    record.setLogin_time(LocalDateTime.now());
    record.setLogout_time(LocalDateTime.now().plusHours(1));
    record.setStatus("FAILED");

    // Act
    LoginRecord savedRecord = loginRecordRepository.save(record);

    // Assert
    assertEquals(3, savedRecord.getEmployee_id());
    assertEquals("10.0.0.1", savedRecord.getIp_address());
    assertEquals("FAILED", savedRecord.getStatus());
    assertNotNull(savedRecord.getLogin_time());
    assertNotNull(savedRecord.getLogout_time());
  }
}
