package login.permission.project.classes.repository;

import login.permission.project.classes.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position,Integer> {
}
