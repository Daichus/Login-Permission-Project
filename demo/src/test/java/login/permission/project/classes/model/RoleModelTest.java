package login.permission.project.classes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Role 模型的單元測試類
 * 測試 Role 實體的基本功能、關聯關係和資料完整性
 */
public class RoleModelTest {

  /**
   * 測試 Role 實體的建構函數和基本屬性
   */
  @Test
  public void testRoleConstructorAndGetters() {
    // 建立測試資料
    Set<Permission> permissions = new HashSet<>();
    permissions.add(new Permission(1, "READ", "category", "description"));

    Role role = new Role(1, "ADMIN", permissions);

    // 驗證基本屬性
    assertEquals(1, role.getRole_id());
    assertEquals("ADMIN", role.getRole());
    assertNotNull(role.getPermissions());
    assertEquals(1, role.getPermissions().size());
  }

  /**
   * 測試 Role 實體的 setter 方法
   */
  @Test
  public void testRoleSetters() {
    Role role = new Role();
    role.setRole_id(1);
    role.setRole("USER");

    Set<Permission> permissions = new HashSet<>();
    permissions.add(new Permission(1, "WRITE", "category", "description"));
    role.setPermissions(permissions);

    // 驗證設置的值
    assertEquals(1, role.getRole_id());
    assertEquals("USER", role.getRole());
    assertEquals(1, role.getPermissions().size());
  }

  /**
   * 測試權限集合的操作
   */
  @Test
  public void testPermissionsCollection() {
    Role role = new Role();
    Set<Permission> permissions = new HashSet<>();

    // 添加權限
    permissions.add(new Permission(1, "READ", "category", "description"));
    permissions.add(new Permission(2, "WRITE", "category", "description"));
    role.setPermissions(permissions);

    // 驗證權限集合
    assertEquals(2, role.getPermissions().size());
    assertTrue(role.getPermissions().stream()
            .anyMatch(p -> p.getPermission_id() == 1));
    assertTrue(role.getPermissions().stream()
            .anyMatch(p -> p.getPermission_id() == 2));
  }

  /**
   * 測試 equals 和 hashCode 方法（由 Lombok @Data 生成）
   */
  @Test
  public void testEqualsAndHashCode() {
    Role role1 = new Role(1, "ADMIN", new HashSet<>());
    Role role2 = new Role(1, "ADMIN", new HashSet<>());
    Role role3 = new Role(2, "USER", new HashSet<>());

    // 測試相等性
    assertEquals(role1, role2);
    assertNotEquals(role1, role3);

    // 測試 HashCode
    assertEquals(role1.hashCode(), role2.hashCode());
    assertNotEquals(role1.hashCode(), role3.hashCode());
  }

  /**
   * 測試 toString 方法（由 Lombok @Data 生成）
   */
  @Test
  public void testToString() {
    Role role = new Role(1, "ADMIN", new HashSet<>());
    String toString = role.toString();

    // 驗證 toString 包含所有重要欄位
    assertTrue(toString.contains("role_id=1"));
    assertTrue(toString.contains("role=ADMIN"));
    assertTrue(toString.contains("permissions=[]"));
  }

  /**
   * 測試空構造函數（由 Lombok @NoArgsConstructor 生成）
   */
  @Test
  public void testNoArgsConstructor() {
    Role role = new Role();

    // 驗證默認值
    assertEquals(0, role.getRole_id());
    assertNull(role.getRole());
    assertNotNull(role.getPermissions());
    assertTrue(role.getPermissions().isEmpty());
  }

  /**
   * 測試添加和移除權限的操作
   */
  @Test
  public void testAddAndRemovePermissions() {
    Role role = new Role();
    Permission permission1 = new Permission(1, "READ", "category", "description");
    Permission permission2 = new Permission(2, "WRITE", "category", "description");

    // 測試添加權限
    role.getPermissions().add(permission1);
    assertEquals(1, role.getPermissions().size());
    assertTrue(role.getPermissions().contains(permission1));

    // 測試添加另一個權限
    role.getPermissions().add(permission2);
    assertEquals(2, role.getPermissions().size());

    // 測試移除權限
    role.getPermissions().remove(permission1);
    assertEquals(1, role.getPermissions().size());
    assertFalse(role.getPermissions().contains(permission1));
    assertTrue(role.getPermissions().contains(permission2));
  }
}