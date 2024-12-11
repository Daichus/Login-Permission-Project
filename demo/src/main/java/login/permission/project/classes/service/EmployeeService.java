package login.permission.project.classes.service;

import login.permission.project.classes.JwtService;
import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import login.permission.project.classes.model.EmployeeManageResponse;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.repository.EmployeeLoginRequestRepository;
import login.permission.project.classes.repository.EmployeeRepository;
import login.permission.project.classes.repository.LoginRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository er;

    @Autowired
    EmployeeLoginRequestRepository elr;

    @Autowired
    LoginRecordRepository lrr;

    @Autowired
    JwtService jwtService;

    @Autowired
    EmailService emailService;

    public List<Employee> getAllEmployees() {
        return er.findAll();
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
                record = lrr.save(record);
                //為避免混淆,登錄紀錄id變數名稱之後需再修改
                int record_id = record.getRecord_id();
                List<String> permission_code = er.getPermissionById(employee.getEmployee_id());

                String JWT_Token = jwtService.generateToken(employee, record_id, permission_code);
                return ResponseEntity.ok(JWT_Token);
            }  else {
                lrr.save(new LoginRecord(employee.getEmployee_id(),"testIpAddress", LocalDateTime.now(),null,"失敗"));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密碼錯誤");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("找不到用戶");
    }

    public List<EmployeeManageResponse> findAllEmployeeManageResponses () {
        return er.findAllEmployeeManageResponses();
    }



}
