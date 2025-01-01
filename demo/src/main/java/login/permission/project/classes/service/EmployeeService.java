package login.permission.project.classes.service;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.*;

import login.permission.project.classes.model.dto.EmployeeRoleDto;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.model.util.ResponseUtil;
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


    public ResponseEntity<?> getAllEmployees(HttpServletRequest request) {
        try{
            jwtUtil.validateRequest(request);
            List<Employee> employees = er.findAll();
            for(Employee employee : employees) {
                employee.setPassword("NaN");
            }
            return ResponseUtil.success("獲取員工資訊成功",employees);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.error("你沒有獲取員工資訊的權限",HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> getEmployeeById(int id, HttpServletRequest request){
        try{
            jwtUtil.validateRequest(request);
            Optional<Employee> employeeOption = er.findById(id);
            if(employeeOption.isPresent()) {
                Employee employee = employeeOption.get();
                employee.setPassword("NaN");
                return ResponseUtil.success("獲取員工資訊成功",employee);
            } else {
                return ResponseUtil.error("找不到指定的員工",HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return ResponseUtil.error("你沒有獲取員工資訊的權限",HttpStatus.UNAUTHORIZED);
        }
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
                return ResponseUtil.error("請先驗證您的電子郵件",HttpStatus.UNAUTHORIZED);
            }

            if(request.getPassword().equals(employee.getPassword())) {
                LoginRecord record = new LoginRecord(employee.getEmployee_id(),"testIpAddress", LocalDateTime.now(),null,"成功");
                record = logRecRepository.save(record);
                //為避免混淆,登錄紀錄id變數名稱之後需再修改
                String employee_id = String.valueOf(employee.getEmployee_id());
                int record_id = record.getRecord_id();

                String JWT_Token = jwtService.generateToken(employee_id, record_id);
                return ResponseEntity.ok(JWT_Token);
            }  else {
                logRecRepository.save(new LoginRecord(employee.getEmployee_id(),"testIpAddress", LocalDateTime.now(),null,"失敗"));
                return ResponseUtil.error("密碼錯誤",HttpStatus.UNAUTHORIZED);
            }
        }
        return ResponseUtil.error("找不到用戶",HttpStatus.NOT_FOUND);
    }


    /**
     *此方法用於設定員工的角色, 前端應傳送結構為:
     * {
     *     employee_id:1
     *     roleIds:[1,2,3]
     * }
     * 的body, JPA將會根據role id尋找相對應的所有Role
     * ,回傳一個Set<Role>,並進行更新員工的角色多對多關聯
     */
    public ResponseEntity<?> setEmployeeRole (EmployeeRoleDto dto, HttpServletRequest request) {
       try {
           //檢查RoleId符合格式與否
           String error = checkRoleDtoId(dto);
           if (error != null) {
               return ResponseUtil.error(error, HttpStatus.BAD_REQUEST);
           }

           jwtUtil.validateRequest(request);
           Optional<Employee> employeeOptional = er.findById(dto.getEmployee_id());
           if(employeeOptional.isPresent()) {
               Set<Integer> roleIds = Arrays.stream(dto.getRoleIds())
                       .map(Integer::parseInt)
                       .collect(Collectors.toSet());
               Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
               Employee employee = employeeOptional.get();
               employee.setRoles(roles);
               er.save(employee);
               return ResponseUtil.success("設定員工角色成功",null);
           } else {
               return ResponseUtil.error("找不到指定id的員工",HttpStatus.NOT_FOUND);
           }
       } catch (IllegalArgumentException e) {
           return ResponseUtil.error("你沒有設定角色的權限",HttpStatus.UNAUTHORIZED);
       }
    }

    private String checkRoleDtoId (EmployeeRoleDto dto) {
            if (dto.getRoleIds() == null) {
                return "角色ID不能為空";
            }
            for (String roleId : dto.getRoleIds()) {
                if (!roleId.matches("\\d+")) {
                    return "角色ID格式只能是數字且不得包含負號: " + roleId;
                }
            }
            return null;
    }






}
