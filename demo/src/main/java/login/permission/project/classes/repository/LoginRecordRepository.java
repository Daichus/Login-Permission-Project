package login.permission.project.classes.repository;

import login.permission.project.classes.model.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoginRecordRepository extends JpaRepository<LoginRecord,Integer> {

    @Query(value = "SELECT * FROM Login_Record WHERE employee_id = :employeeId", nativeQuery = true)
    List<LoginRecord> findByEmployeeId(@Param("employeeId") int employeeId);


}
