package login.permission.project.classes.service;

import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import login.permission.project.classes.repository.EmployeeLoginRequestRepository;
import login.permission.project.classes.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository er;


    public List<Employee> getAllEmployees() {
        return er.findAll();
    }


    public String addEmployee (Employee employee) {
        if(employee !=null){
            er.save(employee);
            return String.format("新增員工成功\n員工名稱: %s\n員工id: %s\n員工職位代號: %s",
                    employee.getName(),
                    employee.getEmployee_id(),
                    employee.getPosition_id());
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


    @Autowired
    EmployeeLoginRequestRepository elr;
    public Employee login(EmployeeLoginRequest request) {
        Optional<Employee> employeeOp = er.findById(request.getEmployee_id());
        if(employeeOp != null) {
            Employee employee = employeeOp.get();
            if(request.getPassword().equals(employee.getPassword())) {
                return employee;
            }
        }
        return null;
    }

}
