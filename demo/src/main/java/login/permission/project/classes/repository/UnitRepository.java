package login.permission.project.classes.repository;

import login.permission.project.classes.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UnitRepository extends JpaRepository<Unit,Integer> {

    @Query("SELECT MAX(u.id) FROM Unit u")
    Integer findMaxId();
}
