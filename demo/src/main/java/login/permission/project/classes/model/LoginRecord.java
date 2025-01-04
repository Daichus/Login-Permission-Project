package login.permission.project.classes.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
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


    public LoginRecord(Integer employee_id, String ip_address, LocalDateTime login_time,
                       LocalDateTime logout_time, String status) {
        this.employee_id = employee_id;
        this.ip_address = ip_address;
        this.login_time = login_time;
        this.logout_time = logout_time;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="record_id")
    private int record_id;   // 保持與資料庫欄位名稱一致

    @Column(name = "employee_id")
    private Integer employee_id;

    @Column(name = "ip_address")
    private String ip_address;  // 改回符合資料庫的命名

    //LocalDateTime 格式 yyyy-MM-ddTHH:mm:ss
    @Column(name = "login_time")
    private LocalDateTime login_time;  // 改回符合資料庫的命名

    @Column(name = "logout_time")
    private LocalDateTime logout_time;  // 改回符合資料庫的命名

    @Column(name = "status")
    private String status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;
}
