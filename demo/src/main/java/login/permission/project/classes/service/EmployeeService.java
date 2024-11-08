package login.permission.project.classes.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
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


    public List<Employee> getAllEmployees() {
        return er.findAll();
    }


    public String addEmployee (Employee employee) {
        if(employee !=null){
            er.save(employee);
            return String.format("新增員工成功\n員工名稱: %s\n員工id: %s\n員工職位代號: %s",
                    employee.getName(),
                    employee.getEmployee_id());
        } else {
            return "添加員工失敗";
        }
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
     * 登錄成功以後回傳JWT　token
     */
    public ResponseEntity<?> login (EmployeeLoginRequest request) {
        Optional<Employee> employeeOp = er.findById(request.getEmployee_id());
        Employee employee;
        if(employeeOp.isPresent()) {
            employee = employeeOp.get();
            if(request.getPassword().equals(employee.getPassword())) {
                LoginRecord record = new LoginRecord(employee.getEmployee_id(),"testIpAddress", LocalDateTime.now(),null,"成功");
                record = lrr.save(record);
                int record_id = record.getRecord_id();
                String JWT_Token = jwtService.generateToken(employee,record_id);
                System.out.println(JWT_Token + "send");
                return ResponseEntity.ok(JWT_Token);
            }  else {
                lrr.save(new LoginRecord(employee.getEmployee_id(),"testIpAddress", null,null,"失敗"));
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("密碼錯誤");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("找不到用戶");
    }

    public List<EmployeeManageResponse> findAllEmployeeManageResponses () {
        return er.findAllEmployeeManageResponses();
    }



}
