package login.permission.project.classes.repository;

import login.permission.project.classes.model.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRecordRepository extends JpaRepository<LoginRecord,Integer> {
}
