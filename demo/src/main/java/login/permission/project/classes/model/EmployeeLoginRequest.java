package login.permission.project.classes.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class EmployeeLoginRequest {

  @Id
  private int employee_id;
  private String password;
}
