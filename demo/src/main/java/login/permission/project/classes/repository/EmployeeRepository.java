package login.permission.project.classes.repository;

import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeManageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT new login.permission.project.classes.model.EmployeeManageResponse(employee.employee_id, employee.name, employee.email, employee.phoneNumber, " +
            "department.department_name, pm.permission_id, unit.unit_name, position.position, status.name) " +
            "FROM Employee employee " +
            "JOIN Status status ON employee.status_id = status.status_id " +
            "JOIN EmpPositionMap empPositionMap ON employee.employee_id = empPositionMap.employee_id " +
            "JOIN Position position ON empPositionMap.position_id = position.position_id " +
            "JOIN PermissionMap pm ON position.position_id = pm.position_id " +
            "JOIN Unit unit ON position.unit_id = unit.unit_id " +
            "JOIN Department department ON unit.department_id = department.department_id")
    List<EmployeeManageResponse> findAllEmployeeManageResponses();




}
