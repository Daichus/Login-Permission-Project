package login.permission.project.classes.repository;

import login.permission.project.classes.model.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
    
    // 查詢前100筆日誌
    List<OperationLog> findTop100ByOrderByOperationTimeDesc();
    
    // 複合條件查詢
    @Query("SELECT o FROM OperationLog o WHERE " +
           "(:userId IS NULL OR o.userId = :userId) AND " +
           "(:operationType IS NULL OR o.operationType = :operationType) AND " +
           "(:module IS NULL OR o.module = :module) AND " +
           "(:startTime IS NULL OR o.operationTime >= :startTime) AND " +
           "(:endTime IS NULL OR o.operationTime <= :endTime)")
    Page<OperationLog> findByConditions(
        @Param("userId") String userId,
        @Param("operationType") String operationType,
        @Param("module") String module,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        Pageable pageable
    );
}