package login.permission.project.classes.model;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Autowired
    JwtService jwtService;

    public   void validateRequest(HttpServletRequest request) {
        Claims claims = jwtService.isTokenValid(request);
        if (claims == null) {
            throw new IllegalArgumentException("未經過授權的操作");
        }
    }

}
