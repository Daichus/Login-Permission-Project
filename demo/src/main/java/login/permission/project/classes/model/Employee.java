package login.permission.project.classes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee的實作.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Employee")
public class Employee {


  @Id
  @Column(name = "employee_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int employee_id;

  @Column(name = "email")
  private String email;

  @Column(name = "name")
  private String name;

  @Column(name = "password")
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;


  @Column(name = "status_id")
  private Integer status_id;

  @ManyToOne
  @JoinColumn(name = "status_id", insertable = false, updatable = false)
  private Status employeeStatus;

  @ManyToMany
  @JoinTable(
          name = "EmpPositionMap",
          joinColumns = @JoinColumn(name = "employee_id"),
          inverseJoinColumns = @JoinColumn(name = "position_id")
  )
  private Set<Position> positions;

  @OneToMany(mappedBy = "employee")
  private Set<LoginRecord> loginRecords;

}
