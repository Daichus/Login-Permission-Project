package login.permission.project.classes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.permission.project.classes.JwtService;

import login.permission.project.classes.model.Permission;
import login.permission.project.classes.model.Role;
import login.permission.project.classes.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwtService;


  private final EmployeeRepository employeeRepository;

  public JwtAuthFilter(JwtService jwtService, EmployeeRepository employeeRepository) {
    this.jwtService = jwtService;
    this.employeeRepository = employeeRepository;
  }

  @Override
  protected void doFilterInternal(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain filterChain
  ) throws ServletException, IOException {
    Claims claims = jwtService.isTokenValid(request);

    if (claims != null) {
      // 從 JWT 中提取 employee_id
      String employeeId = claims.getSubject();

      // 查詢 Employee
      employeeRepository.findById(Integer.parseInt(employeeId))
              .ifPresentOrElse(employee -> {
                // 使用傳統方式提取 permission_code
                List<String> permissionCodes = new ArrayList<>();
                for (Role role : employee.getRoles()) {
                  for (Permission permission : role.getPermissions()) {
                    permissionCodes.add(permission.getPermission_code());
                  }
                }

                // 打印權限代碼以便調試
                System.out.println("權限代碼: " + permissionCodes);

                // 將權限代碼設置到 SecurityContext
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (String code : permissionCodes) {
                  authorities.add(new SimpleGrantedAuthority(code));
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        employeeId,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
              }, () -> {
                // 如果未找到 Employee，返回 403
                try {
                  response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                  response.getWriter().write("Access Denied: Employee not found");
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });






    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.equals("/employee/test/login") ||
            path.startsWith("/error");
  }
}