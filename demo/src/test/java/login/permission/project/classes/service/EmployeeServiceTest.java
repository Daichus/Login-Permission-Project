package login.permission.project.classes.service;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.Role;
import login.permission.project.classes.model.dto.EmployeeRoleDto;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.repository.EmployeeRepository;
import login.permission.project.classes.repository.RoleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;


class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService; // 被測試的類別

    @Mock
    private JwtUtil jwtUtil; // Mock Jwt 工具類

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        //jwtUtil.validateRequest是檢驗jwt token是否合法的函式, 由於不回傳任何值因此利用doNothing跳過
        doNothing().when(jwtUtil).validateRequest(any(HttpServletRequest.class));
    }
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


}