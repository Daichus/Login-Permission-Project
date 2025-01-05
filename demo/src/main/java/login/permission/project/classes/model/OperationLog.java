package login.permission.project.classes.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operation_logs")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userId;
    private String username;
    private LocalDateTime operationTime;
    private String operationType;
    private String module;
    private String description;
    private String ipAddress;
    private String operationResult;
    
    @Column(columnDefinition = "LONGTEXT")
    private String beforeValue;
    
    @Column(columnDefinition = "LONGTEXT")
    private String afterValue;
}