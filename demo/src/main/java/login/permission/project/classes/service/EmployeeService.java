package login.permission.project.classes.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.*;

import login.permission.project.classes.repository.EmployeeRepository;
import login.permission.project.classes.repository.LoginRecordRepository;
import login.permission.project.classes.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {



    @Autowired
    EmployeeRepository er;

//    @Autowired
//    EmployeeLoginRequestRepository elr;

    @Autowired
    LoginRecordRepository logRecRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RoleRepository roleRepository;



    private static final String unAuthMessage = "您沒有權限,或持有的jwt憑證已過期";

    public ResponseEntity<?> getAllEmployees(HttpServletRequest request) {
        Claims claims = jwtService.isTokenValid(request);
        ResponseEntity<?> response;

        if(claims != null) {
            List<Employee> employees = er.findAll();
            for(Employee employee : employees) {
                employee.setPassword("NaN");
            }
            response = ResponseEntity.ok().body(employees);
        } else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(unAuthMessage);
        }
        return response;
    }

    public ResponseEntity<?> getEmployeeById(int id, HttpServletRequest request){
        Claims claims = jwtService.isTokenValid(request);
        ResponseEntity<?> response;

        if(claims != null) {
            Optional<Employee> employeeOption = er.findById(id);
            if(employeeOption.isPresent()) {
                Employee employee = employeeOption.get();
                employee.setPassword("NaN");
                response = ResponseEntity.ok().body(employee);
            } else {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到用戶");
            }

        } else {
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unAuthMessage);
        }
        return response;
    }

    public String addEmployee (Employee employee) {
        if(employee != null){
            // 查詢最大的 employee_id
            Integer maxId = er.findMaxEmployeeId();
            // 如果沒有記錄，從 1 開始，否則 +1
            int newId = (maxId == null) ? 1 : maxId + 1;
            employee.setEmployee_id(newId);

            // 生成驗證 token
            String token = UUID.randomUUID().toString();
            employee.setVerificationToken(token);
            employee.setEnabled(false); // 預設為未驗證

            // 保存員工資訊
            er.save(employee);

            // 發送驗證郵件
            try {
                emailService.sendVerificationEmail(employee.getEmail(), token);
                return String.format("新增員工成功\n員工名稱: %s\n員工id: %d",
                        employee.getName(),
                        employee.getEmployee_id());
            } catch (Exception e) {
                return "驗證郵件發送失敗" + e.getMessage();
            }
        } else {
            return "添加員工失敗";
        }
    }

    // 驗證帳號
    public String verifyAccount(String token) {
        Optional<Employee> employeeOptional = er.findByVerificationToken(token);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setEnabled(true);
            employee.setVerificationToken(null); //清除驗證碼
            er.save(employee);
            return "帳號驗證成功";
        }
        return "無效的驗證碼";
    }

    public String updateEmployee (Employee employee) {
        if(employee != null) {
            er.save(employee);
            return "更新員工資訊完成";
        } else {
            return "更新員工資訊失敗";
        }
    }

    public String deleteEmployee (int id) {
        er.deleteById(id);
        return "刪除員工成功";
    }

    /**
     * 1. 登錄成功以後回傳JWT　token
     * 2. 修改登入方法，加入驗證檢查
     */
    public ResponseEntity<?> login (EmployeeLoginRequest request) {
        Optional<Employee> employeeOp = er.findById(request.getEmployee_id());
        Employee employee;
        if(employeeOp.isPresent()) {
            employee = employeeOp.get();

            // 檢查是否已驗證
            if (!employee.isEnabled()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("請先驗證您的電子郵件！");
            }

            if(request.getPassword().equals(employee.getPassword())) {
                LoginRecord record = new LoginRecord(employee.getEmployee_id(),"testIpAddress", LocalDateTime.now(),null,"成功");
                record = logRecRepository.save(record);
                //為避免混淆,登錄紀錄id變數名稱之後需再修改
                int record_id = record.getRecord_id();
                String employee_id = String.valueOf(employee.getEmployee_id());


                String JWT_Token = jwtService.generateToken(employee_id, record_id);
                return ResponseEntity.ok(JWT_Token);
            }  else {
                logRecRepository.save(new LoginRecord(employee.getEmployee_id(),"testIpAddress", LocalDateTime.now(),null,"失敗"));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密碼錯誤");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("找不到用戶");
    }



    public ResponseEntity<?> setEmployeeRole (EmployeeRoleDto dto, HttpServletRequest request) {
       try {
           jwtUtil.validateRequest(request);
           Optional<Employee> employeeOptional = er.findById(dto.getEmployee_id());
           if(employeeOptional.isPresent()) {
               Set<Integer> roleIds = Arrays.stream(dto.getRoleIds()) // 转换为 Stream
                       .map(Integer::parseInt)                  // 将字符串解析为整数
                       .collect(Collectors.toSet());
               Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
               Employee employee = employeeOptional.get();
               employee.setRoles(roles);

               return ResponseUtil.error("設定員工角色成功",HttpStatus.OK);
           } else {
               return ResponseUtil.error("找不到指定id的員工",HttpStatus.NOT_FOUND);
           }


       } catch (IllegalArgumentException e) {
           return ResponseUtil.error("你沒有設定角色的權限",HttpStatus.UNAUTHORIZED);
       }
    }





}
