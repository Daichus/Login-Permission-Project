package login.permission.project.classes.repository;

import login.permission.project.classes.model.Employee;
import login.permission.project.classes.model.EmployeeLoginRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeLoginRequestRepository extends JpaRepository<EmployeeLoginRequest, Integer> {
}
