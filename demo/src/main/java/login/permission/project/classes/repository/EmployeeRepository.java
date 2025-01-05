package login.permission.project.classes.repository;

import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeManageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {




    @Query("SELECT p.permission_code " +
            "FROM EmpPositionMap e " +
            "JOIN PermissionMap pm ON pm.position_id = e.position_id " +
            "JOIN Permission p ON p.permission_id  = pm.permission_id " +
            "WHERE e.employee_id = :employee_id")
    List<String> getPermissionById(@Param("employee_id") int employee_id);




    // 註冊驗證 Mail
    Optional<Employee> findByVerificationToken(String token);

    @Query("SELECT MAX(e.employee_id) FROM Employee e")
    Integer findMaxEmployeeId();

    // 忘記密碼，找到該員工 Email
    Optional<Employee> findByEmail(String email);
    // 忘記密碼後，Email確認後，有一個重置密碼驗證碼
    Optional<Employee> findByResetPasswordToken(String token);
}
