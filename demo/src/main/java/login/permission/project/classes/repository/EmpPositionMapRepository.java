package login.permission.project.classes.repository;

import login.permission.project.classes.model.EmpPositionMap;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for handling EmpPositionMap entity database operations.
 */
public interface EmpPositionMapRepository extends JpaRepository<EmpPositionMap, Integer> {

}