package login.permission.project.classes.service;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.*;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.repository.EmployeeRepository;
import login.permission.project.classes.repository.LoginRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import login.permission.project.classes.model.ServerResponse;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private JwtUtil jwtUtil;

  @Mock
  private JwtService jwtService;

  @Mock
  private LoginRecordRepository logRecRepository;  // 加入這行

  @Mock
  private EmailService emailService;

  @InjectMocks
  private EmployeeService employeeService;

  Status mockStatus = new Status(); // 根據實際類型初始化
  Set<Role> mockRoles = new HashSet<>();
  Set<LoginRecord> mockLoginRecords = new HashSet<>();

  /**
   *
   * 測試獲取全部員工資訊成功
   */
  @Test
  void getAllEmployees_Success() {
    List<Employee> mockEmployees = Arrays.asList(
            new Employee(1, "aaa@gmail.com", "Alice", "password1", "0911", 1, true, null, mockStatus, mockRoles, mockLoginRecords),
    new Employee(2, "bob@gmail.com", "Bob", "password2", "0912", 2, true, null, mockStatus, mockRoles, mockLoginRecords)
    );
    Mockito.when(employeeRepository.findAll()).thenReturn(mockEmployees);

    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    Mockito.doNothing().when(jwtUtil).validateRequest(mockRequest);

    ResponseEntity<?> response = employeeService.getAllEmployees(mockRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    // 正確處理 ServerResponse
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);

    List<Employee> employees = (List<Employee>) serverResponse.getData();  // 假設 ServerResponse 有 getData 方法
    assertNotNull(employees);
    assertEquals(2, employees.size());
    assertEquals("NaN", employees.get(0).getPassword());
    assertEquals("NaN", employees.get(1).getPassword());
  }

  /**
   *
   * 測試登入成功
   */
  @Test
  void testLogin_Success() {
    // Arrange
    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "password1");
    Employee mockEmployee = new Employee(1, "aaa@gmail.com", "Alice", "password1", "0911", 1, true, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));
    Mockito.when(jwtService.generateToken(Mockito.anyString(), Mockito.anyInt()))
            .thenReturn("mockJWTToken");

    Mockito.when(logRecRepository.save(Mockito.any(LoginRecord.class)))
            .thenReturn(new LoginRecord());

    // Act
    ResponseEntity<?> response = employeeService.login(request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("mockJWTToken", response.getBody());
  }

  /**
   *
   * 測試登入
   * 無效的密碼
   */
  @Test
  void testLogin_InvalidPassword() {
    // Arrange
    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "wrongPassword");
    Employee mockEmployee = new Employee(1, "aaa@gmail.com", "Alice", "password1", "0911", 1, true, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));

    // Act
    ResponseEntity<?> response = employeeService.login(request);

    // Assert
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    // 將回應轉換為 ServerResponse 並驗證
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);
    assertEquals("密碼錯誤", serverResponse.getMessage());
    assertNull(serverResponse.getData());
  }

  /**
   *
   * 測試登入
   * 沒有驗證碼
   */
  @Test
  void testLogin_UnverifiedAccount() {
    // Arrange
    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "password1");
    Employee mockEmployee = new Employee(1, "aaa@gmail.com", "Alice", "password1", "0911", 1, false, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));

    // Act
    ResponseEntity<?> response = employeeService.login(request);

    // Assert
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    // 修改為處理 ServerResponse
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);
    assertEquals("請先驗證您的電子郵件", serverResponse.getMessage());
    assertNull(serverResponse.getData());
  }

  /**
   *
   * 測試新增員工成功
   */
  @Test
  void testAddEmployee_Success() {
    // Arrange
    Employee employee = new Employee();
    employee.setName("Alice");
    employee.setEmail("alice@example.com");

    Mockito.when(employeeRepository.findMaxEmployeeId()).thenReturn(10);
    Mockito.doNothing().when(emailService).sendVerificationEmail(Mockito.anyString(), Mockito.anyString());

    // Act
    String result = employeeService.addEmployee(employee);

    // Assert
    assertTrue(result.contains("新增員工成功"));
    assertEquals(11, employee.getEmployee_id());
  }

  /**
   *
   * 測試新增員工
   * 寄Email
   */
  @Test
  void testAddEmployee_EmailSendFail() {
    // Arrange
    Employee employee = new Employee();
    employee.setName("Alice");
    employee.setEmail("alice@example.com");

    Mockito.when(employeeRepository.findMaxEmployeeId()).thenReturn(10);
    Mockito.doThrow(new RuntimeException("Email server error")).when(emailService).sendVerificationEmail(Mockito.anyString(), Mockito.anyString());

    // Act
    String result = employeeService.addEmployee(employee);

    // Assert
    assertTrue(result.contains("驗證郵件發送失敗"));
  }

  /**
   *
   * 測試驗證碼成功
   */
  @Test
  void testVerifyAccount_Success() {
    // Arrange
    Employee employee = new Employee();
    employee.setEnabled(false);
    employee.setVerificationToken("validToken");

    Mockito.when(employeeRepository.findByVerificationToken("validToken"))
            .thenReturn(Optional.of(employee));

    // Act
    String result = employeeService.verifyAccount("validToken");

    // Assert
    assertEquals("帳號驗證成功", result);
    assertTrue(employee.isEnabled());
    assertNull(employee.getVerificationToken());
  }

  /**
   *
   * 測試驗證碼
   * 無效的Token
   */
  @Test
  void testVerifyAccount_InvalidToken() {
    // Arrange
    Mockito.when(employeeRepository.findByVerificationToken("invalidToken"))
            .thenReturn(Optional.empty());

    // Act
    String result = employeeService.verifyAccount("invalidToken");

    // Assert
    assertEquals("無效的驗證碼", result);
  }

}