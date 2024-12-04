package login.permission.project.classes.repository;

import login.permission.project.classes.model.PermissionMap;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for handling PermissionMap entity database operations.
 */
public interface PermissionMapRepository extends JpaRepository<PermissionMap, Integer> {
}