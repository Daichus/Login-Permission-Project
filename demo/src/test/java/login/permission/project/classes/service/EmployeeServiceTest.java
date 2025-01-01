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
   * 測試獲取全部員工資訊
   * 無權限訪問
   */
  @Test
  void getAllEmployees_Unauthorized() {
    // Arrange
    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    Mockito.doThrow(new IllegalArgumentException()).when(jwtUtil).validateRequest(mockRequest);

    // Act
    ResponseEntity<?> response = employeeService.getAllEmployees(mockRequest);

    // Assert
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);
    assertEquals("你沒有獲取員工資訊的權限", serverResponse.getMessage());
    assertNull(serverResponse.getData());
  }

  /**
   * 測試獲取全部員工資訊
   * 空列表
   */
  @Test
  void getAllEmployees_EmptyList() {
    // Arrange
    List<Employee> mockEmployees = new ArrayList<>();
    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

    Mockito.when(employeeRepository.findAll()).thenReturn(mockEmployees);
    Mockito.doNothing().when(jwtUtil).validateRequest(mockRequest);

    // Act
    ResponseEntity<?> response = employeeService.getAllEmployees(mockRequest);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);
    assertEquals("獲取員工資訊成功", serverResponse.getMessage());

    List<Employee> employees = (List<Employee>) serverResponse.getData();
    assertNotNull(employees);
    assertTrue(employees.isEmpty());
  }

  /**
   * 測試根據ID獲取員工資訊成功
   */
  @Test
  void getEmployeeById_Success() {

    int employeeId = 1;
    Employee mockEmployee = new Employee(
            employeeId,
            "test@gmail.com",
            "Test User",
            "password123",
            "0912345678",
            1,
            true,
            null,
            mockStatus,
            mockRoles,
            mockLoginRecords
    );

    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee));
    Mockito.doNothing().when(jwtUtil).validateRequest(mockRequest);

    ResponseEntity<?> response = employeeService.getEmployeeById(employeeId, mockRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);
    assertEquals("獲取員工資訊成功", serverResponse.getMessage());

    Employee returnedEmployee = (Employee) serverResponse.getData();
    assertNotNull(returnedEmployee);
    assertEquals(employeeId, returnedEmployee.getEmployee_id());
    assertEquals("NaN", returnedEmployee.getPassword());
  }

  /**
   * 測試根據ID獲取員工資訊
   * 找不到指定員工
   */
  @Test
  void getEmployeeById_NotFound() {

    int nonExistentEmployeeId = 999;
    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

    Mockito.when(employeeRepository.findById(nonExistentEmployeeId)).thenReturn(Optional.empty());
    Mockito.doNothing().when(jwtUtil).validateRequest(mockRequest);

    ResponseEntity<?> response = employeeService.getEmployeeById(nonExistentEmployeeId, mockRequest);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);
    assertEquals("找不到指定的員工", serverResponse.getMessage());
    assertNull(serverResponse.getData());
  }

  /**
   * 測試根據ID獲取員工資訊
   * 無權限訪問
   */
  @Test
  void getEmployeeById_Unauthorized() {

    int employeeId = 1;
    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

    Mockito.doThrow(new IllegalArgumentException()).when(jwtUtil).validateRequest(mockRequest);

    ResponseEntity<?> response = employeeService.getEmployeeById(employeeId, mockRequest);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);
    assertEquals("你沒有獲取員工資訊的權限", serverResponse.getMessage());
    assertNull(serverResponse.getData());
  }

  /**
   *
   * 測試登入成功
   */
  @Test
  void testLogin_Success() {

    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "password1");
    Employee mockEmployee = new Employee(1, "aaa@gmail.com", "Alice", "password1", "0911", 1, true, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));
    Mockito.when(jwtService.generateToken(Mockito.anyString(), Mockito.anyInt()))
            .thenReturn("mockJWTToken");

    Mockito.when(logRecRepository.save(Mockito.any(LoginRecord.class)))
            .thenReturn(new LoginRecord());

    ResponseEntity<?> response = employeeService.login(request);

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

    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "wrongPassword");
    Employee mockEmployee = new Employee(1, "aaa@gmail.com", "Alice", "password1", "0911", 1, true, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));

    ResponseEntity<?> response = employeeService.login(request);

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

    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "password1");
    Employee mockEmployee = new Employee(1, "aaa@gmail.com", "Alice", "password1", "0911", 1, false, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));

    ResponseEntity<?> response = employeeService.login(request);

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

    Employee employee = new Employee();
    employee.setName("Alice");
    employee.setEmail("alice@example.com");

    Mockito.when(employeeRepository.findMaxEmployeeId()).thenReturn(10);
    Mockito.doNothing().when(emailService).sendVerificationEmail(Mockito.anyString(), Mockito.anyString());

    String result = employeeService.addEmployee(employee);

    assertTrue(result.contains("新增員工成功"));
    assertEquals(11, employee.getEmployee_id());
  }

  /**
   * 測試新增員工
   * 空值測試
   */
  @Test
  void testAddEmployee_NullEmployee() {
    // Arrange
    Employee employee = null;

    // Act
    String result = employeeService.addEmployee(employee);

    // Assert
    assertEquals("添加員工失敗", result);
    Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any());
    Mockito.verify(emailService, Mockito.never()).sendVerificationEmail(Mockito.anyString(), Mockito.anyString());
  }

  /**
   * 測試新增員工
   * 首筆資料測試 (maxId 為 null)
   */
  @Test
  void testAddEmployee_FirstRecord() {
    // Arrange
    Employee employee = new Employee();
    employee.setName("First Employee");
    employee.setEmail("first@example.com");

    Mockito.when(employeeRepository.findMaxEmployeeId()).thenReturn(null);
    Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenAnswer(invocation -> {
      Employee savedEmployee = invocation.getArgument(0);
      return savedEmployee;
    });
    Mockito.doNothing().when(emailService).sendVerificationEmail(Mockito.anyString(), Mockito.anyString());

    // Act
    String result = employeeService.addEmployee(employee);

    // Assert
    assertTrue(result.contains("新增員工成功"));
    assertEquals(1, employee.getEmployee_id());
    assertNotNull(employee.getVerificationToken());
    assertFalse(employee.isEnabled());
    Mockito.verify(employeeRepository).save(employee);
  }

  /**
   * 測試新增員工
   * 儲存失敗測試
   */
  @Test
  void testAddEmployee_SaveFailed() {
    // Arrange
    Employee employee = new Employee();
    employee.setName("Test Employee");
    employee.setEmail("test@example.com");

    Mockito.when(employeeRepository.findMaxEmployeeId()).thenReturn(5);
    Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
            .thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      employeeService.addEmployee(employee);
    });
    assertTrue(exception.getMessage().contains("Database error"));
    Mockito.verify(emailService, Mockito.never()).sendVerificationEmail(Mockito.anyString(), Mockito.anyString());
  }

  /**
   * 測試新增員工
   * 完整資料驗證測試
   */
  @Test
  void testAddEmployee_FullDataValidation() {
    // Arrange
    Employee employee = new Employee();
    employee.setName("Complete Employee");
    employee.setEmail("complete@example.com");
    employee.setPhoneNumber("0912345678");
    employee.setStatus_id(1);
    employee.setRoles(mockRoles);

    Mockito.when(employeeRepository.findMaxEmployeeId()).thenReturn(20);
    Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenAnswer(invocation -> {
      Employee savedEmployee = invocation.getArgument(0);
      return savedEmployee;
    });
    Mockito.doNothing().when(emailService).sendVerificationEmail(Mockito.anyString(), Mockito.anyString());

    // Act
    String result = employeeService.addEmployee(employee);

    // Assert
    assertTrue(result.contains("新增員工成功"));
    assertEquals(21, employee.getEmployee_id());
    assertNotNull(employee.getVerificationToken());
    assertFalse(employee.isEnabled());
    assertEquals("Complete Employee", employee.getName());
    assertEquals("complete@example.com", employee.getEmail());
    assertEquals("0912345678", employee.getPhoneNumber());
    assertEquals(Integer.valueOf(1), employee.getStatus_id());
    assertEquals(mockRoles, employee.getRoles());
  }

  /**
   *
   * 測試新增員工
   * 寄Email
   */
  @Test
  void testAddEmployee_EmailSendFail() {

    Employee employee = new Employee();
    employee.setName("Alice");
    employee.setEmail("alice@example.com");

    Mockito.when(employeeRepository.findMaxEmployeeId()).thenReturn(10);
    Mockito.doThrow(new RuntimeException("Email server error")).when(emailService).sendVerificationEmail(Mockito.anyString(), Mockito.anyString());

    String result = employeeService.addEmployee(employee);

    assertTrue(result.contains("驗證郵件發送失敗"));
  }

  /**
   *
   * 測試驗證碼成功
   */
  @Test
  void testVerifyAccount_Success() {

    Employee employee = new Employee();
    employee.setEnabled(false);
    employee.setVerificationToken("validToken");

    Mockito.when(employeeRepository.findByVerificationToken("validToken"))
            .thenReturn(Optional.of(employee));

    String result = employeeService.verifyAccount("validToken");

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

    Mockito.when(employeeRepository.findByVerificationToken("invalidToken"))
            .thenReturn(Optional.empty());
    
    String result = employeeService.verifyAccount("invalidToken");

    assertEquals("無效的驗證碼", result);
  }

}