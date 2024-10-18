package login.permission.project.classes.repository;

import login.permission.project.classes.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status,Integer> {
}
