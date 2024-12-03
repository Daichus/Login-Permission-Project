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
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Position實作.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Position")
public class Position {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "position_id")
  private int position_id;


  @Column(name = "position")
  private String position;

  @Column(name = "unit_id")
  private Integer unit_id;

  @ManyToOne
  @JoinColumn(name = "unit_id", insertable = false, updatable = false)
  private Unit unit;

  @ManyToMany(mappedBy = "positions")
  private Set<Employee> employees;

  @ManyToMany
  @JoinTable(
          name = "PermissionMap",
          joinColumns = @JoinColumn(name = "position_id"),
          inverseJoinColumns = @JoinColumn(name = "permission_id")
  )
  private Set<Permission> permissions;
}
