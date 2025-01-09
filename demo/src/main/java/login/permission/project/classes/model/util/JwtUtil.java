package login.permission.project.classes.model.util;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 此方法是將先前Jwt驗證token的方法獨立出,
 * 減少重複的代碼。
 * 驗證未通過會拋出IllegalArgumentException
 */
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
