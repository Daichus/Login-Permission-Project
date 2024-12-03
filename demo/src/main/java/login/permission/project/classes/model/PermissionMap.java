package login.permission.project.classes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing the mapping between positions and permissions.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "PermissionMap")
public class PermissionMap {

  /**
   * Primary key identifier for the permission mapping.
   */
  @Id
  @Column(name = "permissionMap_id")
  private int permissionMapId;

  /**
   * ID of the position in this mapping.
   */
  @Column(name = "position_id")
  private int positionId;

  /**
   * ID of the permission in this mapping.
   */
  @Column(name = "permission_id")
  private int permissionId;
}