package login.permission.project.classes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing the mapping between employees and positions.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "EmpPositionMap")
public class EmpPositionMap {

  /**
   * Primary key identifier for the employee-position mapping.
   */
  @Id
  private int empPositionMapId;

  /**
   * ID of the employee in this mapping.
   */
  @Column(name = "employee_id")
  private Integer employeeId;

  /**
   * ID of the position in this mapping.
   */
  @Column(name = "position_id")
  private Integer positionId;

  /**
   * Employee entity associated with this mapping.
   */
  @ManyToOne
  @JoinColumn(name = "employee_id", insertable = false, updatable = false)
  private Employee employee;

  /**
   * Position entity associated with this mapping.
   */
  @ManyToOne
  @JoinColumn(name = "position_id", insertable = false, updatable = false)
  private Position position;
}