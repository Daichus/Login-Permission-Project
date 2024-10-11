package flu.demo.table_class;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="Login_Record")
public class LoginRecord {

    @Id
    @Column(name="record_id")
    private int record_id;

    @Column(name="employee_id")
    private int employee_id;

    @Column(name="login_time")
    private Date login_time;

    @Column(name="logout_time")
    private Date logout_time;

    @Column(name="ip_address")
    private String ip_address;

    @Column(name="status")
    private String status;


}
