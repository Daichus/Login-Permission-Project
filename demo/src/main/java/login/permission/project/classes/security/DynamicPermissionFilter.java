//package login.permission.project.classes.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class DynamicPermissionFilter extends OncePerRequestFilter {
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String requestURI = request.getRequestURI();
//
//        String permissionCodeHeader = request.getHeader("X-Request-Permission-Code");
//
//        if (permissionCodeHeader != null && !permissionCodeHeader.isEmpty()) {
//            // 將權限代碼分割為數組
//            String[] requestPermissionCodes = permissionCodeHeader.split(",");
//
//            // 構建權限列表
//            Collection<GrantedAuthority> authorities = new ArrayList<>();
//            for (String code : requestPermissionCodes) {
//                authorities.add(new SimpleGrantedAuthority(code.trim()));
//            }
//
//            // 將權限應用到 SecurityContext（不依賴 JWT）
//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    null, // 沒有 Principal
//                    null, // 沒有 Credentials
//                    authorities
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        // 繼續處理請求
//        filterChain.doFilter(request, response);
//    }
//
//}
