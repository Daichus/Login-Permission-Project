package login.permission.project.classes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DynamicPermissionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 從自定義 Header 中提取權限代碼
        String permissionCodeHeader = request.getHeader("X-Request-Permission-Code");

        if (permissionCodeHeader != null && !permissionCodeHeader.isEmpty()) {
            // 將權限代碼分割成列表
            String[] requestPermissionCodes = permissionCodeHeader.split(",");

            // 獲取當前認證信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                Collection<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());

                // 動態添加權限
                for (String code : requestPermissionCodes) {
                    authorities.add(new SimpleGrantedAuthority(code.trim()));
                }

                // 更新認證對象
                Authentication updatedAuth = new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(),
                        authentication.getCredentials(),
                        authorities
                );

                // 更新 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(updatedAuth);
            }
        }

        // 繼續處理請求
        filterChain.doFilter(request, response);
    }
}
