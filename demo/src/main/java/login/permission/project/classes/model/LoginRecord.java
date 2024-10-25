package login.permission.project.classes.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="Login_Record")
public class LoginRecord {

    @Id
    private int record_id;   // 保持與資料庫欄位名稱一致

    @Column(name = "employee_id")
    private Integer employee_id;

    @Column(name = "ip_address")
    private String ip_address;  // 改回符合資料庫的命名

    @Column(name = "login_time")
    private LocalDateTime login_time;  // 改回符合資料庫的命名

    @Column(name = "logout_time")
    private LocalDateTime logout_time;  // 改回符合資料庫的命名

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;
}
