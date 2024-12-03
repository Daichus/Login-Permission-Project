package login.permission.project.classes.repository;

import java.util.List;
import login.permission.project.classes.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * PositionRepository.
 */
public interface PositionRepository extends JpaRepository<Position, Integer> {
  @Query("SELECT p FROM Position p Where(:unitId IS NULL OR p.unit_id = :unitId)"
          + " AND (:positionId IS NULL OR p.position_id = :positionId)")
  List<Position> findByUnitIdAndPositionId(@Param("unitId") Integer unitId,
                                           @Param("positionId") Integer positionId);
}
