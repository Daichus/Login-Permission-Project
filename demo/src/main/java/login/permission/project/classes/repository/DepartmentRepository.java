package login.permission.project.classes.repository;

import login.permission.project.classes.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DepartmentRepository.
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
