package login.permission.project.classes.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="Employee")
public class Employee {


    @Id
    @Column(name = "employee_id")
    private int employee_id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;
//
//    @Column(name = "status_id")
//    private Integer status_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id", referencedColumnName = "unit_id") // 外鍵列
    private Unit unit;

    @Column(name = "enabled")
    private boolean enabled = false;  // 是否已驗證

    @Column(name = "verification_token")
    private String verificationToken;  // 註冊，Email驗證後會返回的驗證碼

    @Column(name = "reset_password_token")
    private String resetPasswordToken;  // 忘記密碼，重置密碼要使用的 token

    @Column(name = "reset_password_token_expiry")
    private LocalDateTime resetPasswordTokenExpiry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "department_id") // 外鍵列
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id", referencedColumnName = "position_id") // 外鍵列
    private Position position;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", insertable = false, updatable = false)
    private Status employeeStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "EmployeeRoleMap",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

//    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private Set<LoginRecord> loginRecords;


}
