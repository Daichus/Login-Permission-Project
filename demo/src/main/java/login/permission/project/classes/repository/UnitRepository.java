package login.permission.project.classes.repository;

import login.permission.project.classes.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit,Integer> {
}
