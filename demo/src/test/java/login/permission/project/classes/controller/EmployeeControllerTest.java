package login.permission.project.classes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.*;
import login.permission.project.classes.security.Config;
import login.permission.project.classes.service.EmployeeService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import({Config.class})
@ComponentScan(basePackages = "login.permission.project.classes.controller")
@AutoConfigureMockMvc
public class EmployeeControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private EmployeeService employeeService;

  @MockBean
  private JwtService jwtService;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  Status mockStatus = new Status();
  Set<Role> mockRoles = new HashSet<>();
  Set<LoginRecord> mockLoginRecords = new HashSet<>();

  @Test
  @DisplayName("獲取全部員工資訊，成功狀態碼 200")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenGetAllEmployees_thenStatus200() throws Exception {
    List<Employee> employees = Arrays.asList(
            new Employee(1, "aaa@gmail.com", "Alice", "password1", "0911111111", 1, true, null, mockStatus, mockRoles, mockLoginRecords),
            new Employee(2, "jjj@gmail.com", "Jon", "password2", "0922222222", 1, true, null, mockStatus, mockRoles, mockLoginRecords)
    );

    Claims mockClaims = new DefaultClaims();
    mockClaims.setSubject("admin");
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "employee_read"));
    mockClaims.setIssuedAt(new Date());
    mockClaims.setExpiration(new Date(System.currentTimeMillis() + 3600000));

    when(jwtService.verifyToken(any())).thenReturn(mockClaims);
    when(employeeService.getAllEmployees(any(HttpServletRequest.class)))
            .thenReturn((ResponseEntity) ResponseEntity.ok(new ServerResponse("Success", employees)));

    mvc.perform(get("/employee/test/get")
                    .header("Authorization", "Bearer mock-token"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Success"))
            .andExpect(jsonPath("$.data[0].employee_id").value(1))
            .andExpect(jsonPath("$.data[0].name").value("Alice"))
            .andExpect(jsonPath("$.data[1].employee_id").value(2))
            .andExpect(jsonPath("$.data[1].name").value("Jon"));
  }

  @Test
  @DisplayName("獲取全部員工資訊，沒有權杖，未授權狀態碼 401")
  public void whenGetAllEmployees_noToken_thenStatus401() throws Exception {
    mvc.perform(get("/employee/test/get"))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Token not provided."));
  }

  @Test
  @DisplayName("獲取全部員工資訊，提供無效權杖，未授權狀態碼 401")
  public void whenGetAllEmployees_invalidToken_thenStatus401() throws Exception {
    when(jwtService.verifyToken(any())).thenReturn(null);

    mvc.perform(get("/employee/test/get")
                    .header("Authorization", "Bearer invalid-token"))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Invalid token."));
  }

  @Test
  @DisplayName("用 ID 獲取該員工資訊，成功狀態碼 200")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenGetEmployeeById_thenStatus200() throws Exception {
    int employeeId = 1;
    Employee employee = new Employee(
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

    Claims mockClaims = new DefaultClaims();
    mockClaims.setSubject("admin");
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "employee_read"));

    when(jwtService.verifyToken(any())).thenReturn(mockClaims);
    when(employeeService.getEmployeeById(ArgumentMatchers.eq(employeeId), any(HttpServletRequest.class)))
            .thenReturn((ResponseEntity) ResponseEntity.ok(new ServerResponse("獲取員工資訊成功", employee)));

    mvc.perform(post("/employee/test/getEmployeeById/" + employeeId)
                    .header("Authorization", "Bearer mock-token"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("獲取員工資訊成功"))
            .andExpect(jsonPath("$.data.employee_id").value(employeeId))
            .andExpect(jsonPath("$.data.name").value("Test User"));
  }

  @Test
  @DisplayName("用 ID 獲取該員工資訊，找不到 ID，找不到資源狀態碼 404")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenGetEmployeeById_notFound_thenStatus404() throws Exception {
    int employeeId = 999;

    Claims mockClaims = new DefaultClaims();
    mockClaims.setSubject("admin");
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "employee_read"));

    when(jwtService.verifyToken(any())).thenReturn(mockClaims);
    when(employeeService.getEmployeeById(ArgumentMatchers.eq(employeeId), any(HttpServletRequest.class)))
            .thenReturn((ResponseEntity) ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServerResponse("找不到指定的員工", null)));

    mvc.perform(post("/employee/test/getEmployeeById/" + employeeId)
                    .header("Authorization", "Bearer mock-token"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("找不到指定的員工"));
  }

  @Test
  @DisplayName("用 ID 獲取該員工資訊，無效 ID，失敗狀態碼 400")
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void whenGetEmployeeById_invalidId_thenStatus400() throws Exception {
    Claims mockClaims = new DefaultClaims();
    mockClaims.setSubject("admin");
    mockClaims.put("permissionCode", Arrays.asList("ADMIN", "employee_read"));

    when(jwtService.verifyToken(any())).thenReturn(mockClaims);

    mvc.perform(post("/employee/test/getEmployeeById/invalid-id")
                    .header("Authorization", "Bearer mock-token"))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }
}