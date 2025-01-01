package login.permission.project.classes.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoginRecord 模型的單元測試類
 * 測試 LoginRecord 實體的基本功能、屬性、關聯關係和資料完整性
 */
public class LoginRecordModelTest {

  /**
   * 測試 LoginRecord 實體的建構函數和基本屬性
   */
  @Test
  public void testLoginRecordConstructorAndGetters() {
    // Arrange
    LocalDateTime loginTime = LocalDateTime.now();
    LocalDateTime logoutTime = LocalDateTime.now();
    LoginRecord loginRecord = new LoginRecord(1, "127.0.0.1", loginTime, logoutTime, "SUCCESS");

    // Assert
    assertEquals(1, loginRecord.getEmployee_id());
    assertEquals("127.0.0.1", loginRecord.getIp_address());
    assertEquals(loginTime, loginRecord.getLogin_time());
    assertEquals(logoutTime, loginRecord.getLogout_time());
    assertEquals("SUCCESS", loginRecord.getStatus());
  }

  /**
   * 測試 LoginRecord 實體的 setter 方法
   */
  @Test
  public void testLoginRecordSetters() {
    // Arrange
    LoginRecord loginRecord = new LoginRecord();
    LocalDateTime loginTime = LocalDateTime.now();
    LocalDateTime logoutTime = LocalDateTime.now();

    // Act
    loginRecord.setEmployee_id(2);
    loginRecord.setIp_address("192.168.0.1");
    loginRecord.setLogin_time(loginTime);
    loginRecord.setLogout_time(logoutTime);
    loginRecord.setStatus("FAILED");

    // Assert
    assertEquals(2, loginRecord.getEmployee_id());
    assertEquals("192.168.0.1", loginRecord.getIp_address());
    assertEquals(loginTime, loginRecord.getLogin_time());
    assertEquals(logoutTime, loginRecord.getLogout_time());
    assertEquals("FAILED", loginRecord.getStatus());
  }

  /**
   * 測試 equals 和 hashCode 方法
   */
  @Test
  public void testEqualsAndHashCode() {
    // Arrange
    LocalDateTime loginTime = LocalDateTime.now();
    LocalDateTime logoutTime = LocalDateTime.now();

    LoginRecord loginRecord1 = new LoginRecord(1, "127.0.0.1", loginTime, logoutTime, "SUCCESS");
    LoginRecord loginRecord2 = new LoginRecord(1, "127.0.0.1", loginTime, logoutTime, "SUCCESS");
    LoginRecord loginRecord3 = new LoginRecord(2, "192.168.0.1", loginTime, logoutTime, "FAILED");

    // Assert
    assertEquals(loginRecord1, loginRecord2);
    assertNotEquals(loginRecord1, loginRecord3);

    assertEquals(loginRecord1.hashCode(), loginRecord2.hashCode());
    assertNotEquals(loginRecord1.hashCode(), loginRecord3.hashCode());
  }

  /**
   * 測試 toString 方法
   */
  @Test
  public void testToString() {
    // Arrange
    LocalDateTime loginTime = LocalDateTime.now();
    LoginRecord loginRecord = new LoginRecord(1, "127.0.0.1", loginTime, null, "SUCCESS");

    // Act
    String toString = loginRecord.toString();

    // Assert
    assertTrue(toString.contains("employee_id=1"));
    assertTrue(toString.contains("ip_address=127.0.0.1"));
    assertTrue(toString.contains("status=SUCCESS"));
  }

  /**
   * 測試無參數建構函數
   */
  @Test
  public void testNoArgsConstructor() {
    // Arrange
    LoginRecord loginRecord = new LoginRecord();

    // Assert
    assertEquals(0, loginRecord.getRecord_id());
    assertNull(loginRecord.getIp_address());
    assertNull(loginRecord.getLogin_time());
    assertNull(loginRecord.getLogout_time());
    assertNull(loginRecord.getStatus());
  }

  /**
   * 測試 Employee 關聯
   */
  @Test
  public void testEmployeeAssociation() {
    // Arrange
    Employee employee = new Employee();
    employee.setEmployee_id(1);
    employee.setName("Test Employee");

    LoginRecord loginRecord = new LoginRecord();
    loginRecord.setEmployee(employee);

    // Assert
    assertNotNull(loginRecord.getEmployee());
    assertEquals(1, loginRecord.getEmployee().getEmployee_id());
    assertEquals("Test Employee", loginRecord.getEmployee().getName());
  }

  /**
   * 測試狀態欄位的邊界條件
   */
  @Test
  public void testStatusFieldValidation() {
    // Arrange
    LoginRecord loginRecord = new LoginRecord();

    // Act & Assert
    loginRecord.setStatus("SUCCESS");
    assertEquals("SUCCESS", loginRecord.getStatus());

    loginRecord.setStatus("FAILED");
    assertEquals("FAILED", loginRecord.getStatus());

    loginRecord.setStatus("");
    assertEquals("", loginRecord.getStatus());

    loginRecord.setStatus(null);
    assertNull(loginRecord.getStatus());
  }

  /**
   * 測試記錄時間的合法性
   */
  @Test
  public void testLoginAndLogoutTimeValidation() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime future = now.plusHours(1);

    LoginRecord loginRecord = new LoginRecord();

    // Act
    loginRecord.setLogin_time(now);
    loginRecord.setLogout_time(future);

    // Assert
    assertEquals(now, loginRecord.getLogin_time());
    assertEquals(future, loginRecord.getLogout_time());
    assertTrue(loginRecord.getLogout_time().isAfter(loginRecord.getLogin_time()));
  }

  /**
   * 測試登入登出時間為 null 的情況
   */
  @Test
  public void testNullLoginAndLogoutTime() {
    // Arrange
    LoginRecord loginRecord = new LoginRecord();

    // Act
    loginRecord.setLogin_time(null);
    loginRecord.setLogout_time(null);

    // Assert
    assertNull(loginRecord.getLogin_time());
    assertNull(loginRecord.getLogout_time());
  }
}
