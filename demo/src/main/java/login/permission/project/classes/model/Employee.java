package login.permission.project.classes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="Employee")
public class Employee {


    @Id
    @Column(name="employee_id")
    private int employee_id;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="email")
    private String email;

    @Column(name="phone_number")
    private String phone_number;

    @Column(name="position_id")
    private int position_id;

    @Column(name="status_id")
    private  int status;






}
