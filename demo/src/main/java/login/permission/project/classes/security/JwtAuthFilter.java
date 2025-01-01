package login.permission.project.classes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.permission.project.classes.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  public JwtAuthFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain filterChain
  ) throws ServletException, IOException {
    // 首先檢查是否有 Authorization header
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"message\":\"Token not provided.\"}");
      return;  // 直接返回，不執行 filterChain
    }

    Claims claims = jwtService.isTokenValid(request);

    if (claims == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"message\":\"Invalid token.\"}");
      return;  // 直接返回，不執行 filterChain
    }

    List<String> permissions = (List<String>) claims.get("permissionCode");

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            claims.getSubject(),
            null,
            permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList())
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();
    return path.equals("/employee/test/login") ||
            path.startsWith("/error");
  }
}