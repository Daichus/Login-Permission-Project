package login.permission.project.classes.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing system permissions.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Permission")
public class Permission {

  /**
   * Primary key identifier for the permission.
   */
  @Id
  @Column(name = "permission_id")
  private int permissionId;

  /**
   * Name of the permission.
   */
  @Column(name = "permission_name")
  private String permissionName;

  /**
   * Description of what the permission allows.
   */
  @Column(name = "description")
  private String description;

  /**
   * Unique code identifying the permission.
   */
  @Column(name = "permission_code")
  private String permissionCode;

  /**
   * Set of positions that have this permission.
   */
  @ManyToMany(mappedBy = "permissions")
  private Set<Position> positions;
}