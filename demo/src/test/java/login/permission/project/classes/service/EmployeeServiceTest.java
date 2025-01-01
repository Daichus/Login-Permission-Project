package login.permission.project.classes.service;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.*;
import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.Role;
import login.permission.project.classes.model.dto.EmployeeRoleDto;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.repository.EmployeeRepository;
import login.permission.project.classes.repository.LoginRecordRepository;
import login.permission.project.classes.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import login.permission.project.classes.model.ServerResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private JwtUtil jwtUtil; // Mock Jwt 工具類

  @Mock
  private JwtService jwtService;

  @Mock
  private LoginRecordRepository logRecRepository;  // 加入這行

  @Mock
  private EmailService emailService;

    @Mock
    private RoleRepository roleRepository;

  @InjectMocks
  private EmployeeService employeeService; // 被測試的類別

  Status mockStatus = new Status(); // 根據實際類型初始化
  Set<Role> mockRoles = new HashSet<>();
  Set<LoginRecord> mockLoginRecords = new HashSet<>();

//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.openMocks(this);
//
//    //jwtUtil.validateRequest是檢驗jwt token是否合法的函式, 由於不回傳任何值因此利用doNothing跳過
//    doNothing().when(jwtUtil).validateRequest(any(HttpServletRequest.class));
//  }
  /**
   * 測試設對象：設定員工角色的方法 setEmployeeRole 是否可以正確設定員工的角色
   * 測試重點：
   * 1. 測試該方法必須提供 EmployeeRoleDto 類別物件，以及一個至少包含一個 RoleId 的陣列作為參數。
   *    測試員工 ID 為 1，角色 ID 為 ["1", "2", "3"] 的正常情況。
   * 2. 測試需要模擬 setEmployeeRole 中會使用到的 employeeRepository 與 roleRepository 介面的行為。
   *    由於這些介面會直接連接到資料庫，為避免真實的數據庫操作，使用 Mock 介面模擬其行為，並返回測試所需的數據。
   * 測試步驟：
   * 1. 初始化測試數據，包括 EmployeeRoleDto、Employee 和 Role 對象。
   * 2. 模擬 jwtUtil.validateRequest 方法，跳過 JWT 驗證邏輯。
   * 3. 模擬 employeeRepository 和 roleRepository 的查詢與保存行為，返回設置的測試數據。
   * 4. 調用 setEmployeeRole 方法，並驗證返回的 Response 是否為 200（操作成功）。
   * 5. 使用 verify 驗證 save 和 findAllById 方法是否被正確調用，且僅調用一次。
   */
  @Test
  @DisplayName("測試setEmployeeRole執行成功時的情況")
  void testSetRole () {
    //先設定一個EmployeeRoleDto物件, 提供一個員工id以及一組角色的類別id
    EmployeeRoleDto dto = new EmployeeRoleDto();
    dto.setEmployee_id(1);
    dto.setRoleIds(new String[]{"1", "2", "3"});

    //設定一個employee物件以供employeeRepository模擬用
    Employee employee = new Employee();
    employee.setEmployee_id(1);

    //設定三個Role物件以供roleRepository模擬用
    Role role1 = new Role();
    Role role2 = new Role();
    Role role3 = new Role();
    Set<Role> roles = new HashSet<>(Arrays.asList(role1, role2, role3));

    //模擬employeeRepository調用findById(1)時返回這裡創建的employee物件
    when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

    //模擬employeeRepository調用findAllById時返回這裡創建的roles Set
    when(roleRepository.findAllById(anySet())).thenReturn(new ArrayList<>(roles));

    //模擬employeeRepository.save時返回這裡創建的employee物件
    when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

    //開始測試方法
    ResponseEntity<?> response = employeeService.setEmployeeRole(dto, mock(HttpServletRequest.class));

    // 驗證Response結果是不是200
    assertEquals(HttpStatus.OK, response.getStatusCode());

    //驗證employeeRepository的save方法有沒有被調用
    verify(employeeRepository, times(1)).save(employee);

    //驗證roleRepository的findAllById方法有沒有被調用
    verify(roleRepository, times(1)).findAllById(anySet());
  }

  @Test
  @DisplayName("模擬找不到員工的情形")
  void testSetEmployeeId_Employee_not_found() {
    EmployeeRoleDto dto = new EmployeeRoleDto();
    dto.setEmployee_id(1);
    dto.setRoleIds(new String[]{"1","2"});

    when(employeeRepository.findById(1)).thenReturn(Optional.empty());

    ResponseEntity<?> response = employeeService.setEmployeeRole(dto, mock(HttpServletRequest.class));

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }


  @Test
  @DisplayName("測試驗證Jwt token不通過的情形")
  void testSetEmployeeRole_InvalidToken() {
    EmployeeRoleDto dto = new EmployeeRoleDto();
    dto.setEmployee_id(1);
    dto.setRoleIds(new String[]{"1", "2"});
    doThrow(new IllegalArgumentException("JWT格式不正確"))
            .when(jwtUtil).validateRequest(any(HttpServletRequest.class));

    ResponseEntity<?> response = employeeService.setEmployeeRole(dto, mock(HttpServletRequest.class));

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNotNull(response.getBody());
    System.out.println(response.getBody());
  }

  @ParameterizedTest
  @DisplayName("測驗Role Id格式錯誤的情形")
  @CsvSource({
          "'1,abc,3'",
          "'1,-2,3'",
          "'-1,-2,-3'",
          "'#1,2,3'",
          "'1 1,2,3'",
          "''"
  })
  void testSetEmployeeRole_InvalidRoleId (String roleIdsCsv) {
    EmployeeRoleDto dto = new EmployeeRoleDto();
    dto.setEmployee_id(1);
    if (!roleIdsCsv.isEmpty()) {
      dto.setRoleIds(roleIdsCsv.split(","));
    } else {
      dto.setRoleIds(null);
    }
    ResponseEntity<?> response = employeeService.setEmployeeRole(dto, mock(HttpServletRequest.class));
    System.out.println("Response Body: " + response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }



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
   * 測試登入
   * 找不到用戶
   */
  @Test
  void testLogin_UserNotFound() {
    // Arrange
    EmployeeLoginRequest request = new EmployeeLoginRequest(999, "password1");
    Mockito.when(employeeRepository.findById(999)).thenReturn(Optional.empty());

    // Act
    ResponseEntity<?> response = employeeService.login(request);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    ServerResponse serverResponse = (ServerResponse) response.getBody();
    assertNotNull(serverResponse);
    assertEquals("找不到用戶", serverResponse.getMessage());
    assertNull(serverResponse.getData());

    // 驗證沒有創建登入記錄
    Mockito.verify(logRecRepository, Mockito.never()).save(Mockito.any(LoginRecord.class));
  }

  /**
   * 測試登入失敗
   * 保存登入記錄異常
   */
  @Test
  void testLogin_SaveLoginRecordError() {
    // Arrange
    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "password1");
    Employee mockEmployee = new Employee(1, "test@gmail.com", "Test", "password1", "0911", 1, true, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));
    Mockito.when(logRecRepository.save(Mockito.any(LoginRecord.class)))
            .thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      employeeService.login(request);
    });
    assertTrue(exception.getMessage().contains("Database error"));
  }

  /**
   * 測試登入失敗
   * JWT 生成異常
   */
  @Test
  void testLogin_JwtGenerationError() {
    // Arrange
    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "password1");
    Employee mockEmployee = new Employee(1, "test@gmail.com", "Test", "password1", "0911", 1, true, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));
    Mockito.when(logRecRepository.save(Mockito.any(LoginRecord.class)))
            .thenReturn(new LoginRecord());
    Mockito.when(jwtService.generateToken(Mockito.anyString(), Mockito.anyInt()))
            .thenThrow(new RuntimeException("JWT generation error"));

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      employeeService.login(request);
    });
    assertTrue(exception.getMessage().contains("JWT generation error"));
  }

  /**
   * 測試登入失敗
   * 密碼錯誤時的登入記錄
   */
  @Test
  void testLogin_InvalidPasswordLoginRecord() {
    // Arrange
    EmployeeLoginRequest request = new EmployeeLoginRequest(1, "wrongPassword");
    Employee mockEmployee = new Employee(1, "test@gmail.com", "Test", "correctPassword", "0911", 1, true, null, mockStatus, mockRoles, mockLoginRecords);

    Mockito.when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));

    // Act
    ResponseEntity<?> response = employeeService.login(request);

    // Assert
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    // 驗證保存失敗的登入記錄
    ArgumentCaptor<LoginRecord> loginRecordCaptor = ArgumentCaptor.forClass(LoginRecord.class);
    Mockito.verify(logRecRepository).save(loginRecordCaptor.capture());

    LoginRecord savedRecord = loginRecordCaptor.getValue();
    assertEquals(1, savedRecord.getEmployee_id());
    assertEquals("testIpAddress", savedRecord.getIp_address());
    assertEquals("失敗", savedRecord.getStatus());
    assertNotNull(savedRecord.getLogin_time());
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

  /**
   * 測試更新員工資訊成功
   */
  @Test
  void testUpdateEmployee_Success() {
    // Arrange
    Employee employee = new Employee(
            1,
            "updated@gmail.com",
            "Updated Name",
            "password123",
            "0987654321",
            1,
            true,
            null,
            mockStatus,
            mockRoles,
            mockLoginRecords
    );

    Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
            .thenReturn(employee);

    // Act
    String result = employeeService.updateEmployee(employee);

    // Assert
    assertEquals("更新員工資訊完成", result);
    Mockito.verify(employeeRepository).save(employee);
  }

  /**
   * 測試更新員工資訊
   * 空值測試
   */
  @Test
  void testUpdateEmployee_NullEmployee() {
    // Arrange
    Employee employee = null;

    // Act
    String result = employeeService.updateEmployee(employee);

    // Assert
    assertEquals("更新員工資訊失敗", result);
    Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any());
  }

  /**
   * 測試更新員工資訊
   * 資料庫異常
   */
  @Test
  void testUpdateEmployee_DatabaseError() {
    // Arrange
    Employee employee = new Employee();
    employee.setEmployee_id(1);
    employee.setName("Test Employee");

    Mockito.when(employeeRepository.save(Mockito.any(Employee.class)))
            .thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      employeeService.updateEmployee(employee);
    });
    assertTrue(exception.getMessage().contains("Database error"));
  }

  /**
   * 測試刪除員工成功
   */
  @Test
  void testDeleteEmployee_Success() {
    // Arrange
    int employeeId = 1;
    Mockito.doNothing().when(employeeRepository).deleteById(employeeId);

    // Act
    String result = employeeService.deleteEmployee(employeeId);

    // Assert
    assertEquals("刪除員工成功", result);
    Mockito.verify(employeeRepository).deleteById(employeeId);
  }

  /**
   * 測試刪除員工
   * 員工不存在
   */
  @Test
  void testDeleteEmployee_NotFound() {
    // Arrange
    int nonExistentId = 999;
    Mockito.doThrow(new RuntimeException("Employee not found"))
            .when(employeeRepository).deleteById(nonExistentId);

    // Act & Assert
    Exception exception = assertThrows(RuntimeException.class, () -> {
      employeeService.deleteEmployee(nonExistentId);
    });
    assertTrue(exception.getMessage().contains("Employee not found"));
  }

}