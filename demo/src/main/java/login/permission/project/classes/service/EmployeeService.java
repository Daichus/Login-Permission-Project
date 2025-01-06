package login.permission.project.classes.service;

import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.*;

import login.permission.project.classes.model.dto.EmployeeUpdateDto;
import login.permission.project.classes.model.util.JwtUtil;
import login.permission.project.classes.model.util.ResponseUtil;
import login.permission.project.classes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

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

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    StatusRepository statusRepository;


    public ResponseEntity<?> getAllEmployees(HttpServletRequest request) {
        try{
            jwtUtil.validateRequest(request);
            List<Employee> employees = employeeRepository.findAll();
//            for(Employee employee : employees) {
//                employee.setPassword("NaN");
//            }
            return ResponseUtil.success("獲取員工資訊成功",employees);
        } catch (IllegalArgumentException e) {
            return ResponseUtil.error("你沒有獲取員工資訊的權限",HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> getEmployeeById(int id, HttpServletRequest request){
        try{
            jwtUtil.validateRequest(request);
            Optional<Employee> employeeOption = employeeRepository.findById(id);
            if(employeeOption.isPresent()) {
                Employee employee = employeeOption.get();
//                employee.setPassword("NaN");
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
            Integer maxId = employeeRepository.findMaxEmployeeId();
            // 如果沒有記錄，從 1 開始，否則 +1
            int newId = (maxId == null) ? 1 : maxId + 1;
            employee.setEmployee_id(newId);

            // 生成驗證 token
            String token = UUID.randomUUID().toString();
            employee.setVerificationToken(token);
            employee.setEnabled(false); // 預設為未驗證

            // 保存員工資訊
            employeeRepository.save(employee);

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
        Optional<Employee> employeeOptional = employeeRepository.findByVerificationToken(token);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setEnabled(true);
            employee.setVerificationToken(null); //清除驗證碼
            employeeRepository.save(employee);
            return "帳號驗證成功";
        }
        return "無效的驗證碼";
    }

    public String updateEmployee (Employee employee) {
        if(employee != null) {
            Employee existingEmployee = employeeRepository.findById(employee.getEmployee_id()).orElse(null);
            if(existingEmployee != null) {
                // 保留原有的密碼
                employee.setPassword(existingEmployee.getPassword());
                employeeRepository.save(employee);
                return "更新員工資訊成功";
            }
        }
        return "更新員工資訊失敗";
    }

    public String deleteEmployee (int id) {
        employeeRepository.deleteById(id);
        return "刪除員工成功";
    }

    /**
     * 1. 登錄成功以後回傳JWT　token
     * 2. 修改登入方法，加入驗證檢查
     */
    public ResponseEntity<?> login (EmployeeLoginRequest request) {
        Optional<Employee> employeeOp = employeeRepository.findById(request.getEmployee_id());
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

                // 異步發送登入通知郵件
                CompletableFuture.runAsync(() -> {
                    try {
                        emailService.sendLoginNotification(employee);
                    } catch (Exception e) {
                        // 記錄錯誤但不影響登入流程
                        System.err.println("發送登入通知郵件失敗: " + e.getMessage());
                    }
                });

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
     *
     * 忘記密碼
     * 發送重置密碼郵件
     */
    public ResponseEntity<?> requestPasswordReset(String email) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);
        if (employeeOpt.isEmpty()) {
            return ResponseUtil.error("找不到該 Email 對應的員工", HttpStatus.NOT_FOUND);
        }

        Employee employee = employeeOpt.get();
        String token = UUID.randomUUID().toString();
        employee.setResetPasswordToken(token);
        employee.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
        employeeRepository.save(employee);

        try {
            emailService.sendResetPasswordEmail(email, token);
            return ResponseUtil.success("重置密碼郵件已發送", null);
        } catch (Exception e) {
            return ResponseUtil.error("郵件發送失敗", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 驗證重置密碼的 token 並更新密碼
    public ResponseEntity<?> resetPassword(String token, String newPassword) {
        Optional<Employee> employeeOpt = employeeRepository.findByResetPasswordToken(token);
        if (employeeOpt.isEmpty()) {
            return ResponseUtil.error("無效的重置連結", HttpStatus.BAD_REQUEST);
        }

        Employee employee = employeeOpt.get();
        if (employee.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseUtil.error("重置連結已過期", HttpStatus.BAD_REQUEST);
        }

        // 更新密碼
        employee.setPassword(newPassword);
        employee.setResetPasswordToken(null);
        employee.setResetPasswordTokenExpiry(null);
        employeeRepository.save(employee);

        return ResponseEntity.ok(Map.of("success", true, "message", "密碼重置成功"));
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
    public ResponseEntity<?> updateEmployee(EmployeeUpdateDto dto ) {
           Optional<Employee> employeeOptional = employeeRepository.findById(dto.getEmployee_id());
           if(employeeOptional.isPresent()) {
               //更新角色
               Employee employee = employeeOptional.get();
               updateEmployee(employee, dto);
               return ResponseUtil.error("設定員工角色成功",HttpStatus.OK);
           } else {
               return ResponseUtil.error("找不到指定id的員工",HttpStatus.NOT_FOUND);
           }

    }

    private Set<Integer> getRoleIdSet (EmployeeUpdateDto dto) {
        return Arrays.stream(dto.getRoleIds())
                .filter(Objects::nonNull) // 排除空值
                .map(Integer::valueOf) // 将 String 转换为 Integer
                .collect(Collectors.toSet());
    }

    private void updateEmployee(Employee employee,EmployeeUpdateDto dto) {
        Set<Integer> roleIds = getRoleIdSet(dto);
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        employee.setRoles(roles);
        //更新姓名
        employee.setName(dto.getName());
        //更新email
        employee.setEmail(dto.getEmail());
        //更新電話
        employee.setPhoneNumber(dto.getPhoneNumber());
        // 更新部門（多對一）
        if(dto.getDepartment_id() > 0) {
            departmentRepository.findById(dto.getDepartment_id())
                    .ifPresent(employee::setDepartment);
        }
        // 更新單位（多對一）
        if(dto.getUnit_id() > 0) {
            unitRepository.findById(dto.getUnit_id())
                    .ifPresent(employee::setUnit);
        }
        // 更新職位（多對一）
        if(dto.getPosition_id() > 0) {
            positionRepository.findById(dto.getPosition_id())
                    .ifPresent(employee::setPosition);
        }
        // 更新狀態（多對一）
        if(dto.getStatus_id() >= 0) {
            statusRepository.findById(dto.getStatus_id())
                    .ifPresent(employee::setEmployeeStatus);
        }
        employeeRepository.save(employee);
    }




}
