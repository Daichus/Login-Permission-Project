package login.permission.project.classes.repository;

import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeManageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT new login.permission.project.classes.model.EmployeeManageResponse(employee.employee_id, employee.name, employee.email, employee.phoneNumber, " +
            "department.department_name, pm.permission_id, unit.unit_name, position.position, status.name) " +
            "FROM Employee employee " +
            "JOIN Status status ON employee.status_id = status.status_id " +  // 加上空格
            "JOIN EmpPositionMap empPositionMap ON employee.employee_id = empPositionMap.employee_id " +  // 加上空格
            "JOIN Position position ON empPositionMap.position_id = position.position_id " +  // 加上空格
            "JOIN Unit unit ON position.unit_id = unit.unit_id " +  // 加上空格
            "JOIN Department department ON unit.department_id = department.department_id " +  // 加上空格
            "JOIN PermissionMap pm ON pm.position_id = empPositionMap.position_id")
    List<EmployeeManageResponse> findAllEmployeeManageResponses();

    @Query("SELECT pm.permission_id " +
            "FROM EmpPositionMap e " +
            "JOIN PermissionMap pm ON pm.position_id = e.position_id " +
            "WHERE e.employee_id = :employee_id")
    List<Integer> getPermissionById(@Param("employee_id") int employee_id);





}
