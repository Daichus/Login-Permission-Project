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

        // 獲取 `requestPermissionCode`，支持 JSON 格式或 query parameter 格式
        String[] requestPermissionCode = request.getParameterValues("requestPermissionCode");

        // 如果有傳入 `requestPermissionCode`
        if (requestPermissionCode != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 確保用戶已經認證
            if (authentication != null && authentication.isAuthenticated()) {
                // 動態添加權限
                Collection<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
                for (String code : requestPermissionCode) {
                    authorities.add(new SimpleGrantedAuthority(code));
                }

                // 更新認證對象
                Authentication updatedAuth = new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(),
                        authentication.getCredentials(),
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(updatedAuth);
            }
        }

        // 繼續處理請求
        filterChain.doFilter(request, response);
    }
}
