package login.permission.project.classes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import login.permission.project.classes.model.Employee;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {


    private static final String KEY = "5k4g4ji32k7ru8au4m4ul4t6su3h9j142l4g45k4g4uke7rul4mpv4cl31j4cl3";

    private static final long EXPIRATION_TIME = 3600000; //毫秒為單位,每小時使用者需重新登錄

    /**
     * 用於生成JWT Token的方法
     *JWT包含Header,Payload,Signature三個部分
     *方法中需要手動設定JWT的Payload以定義要傳送回前端的訊息
     */
    public String generateToken(Employee employee) {
        Map<String, Object> userData = new HashMap<>();
        String employee_id = String.valueOf(employee.getEmployee_id());
        userData.put("userName", employee.getName());
        userData.put("userEmail", employee.getEmail());
        userData.put("userPhone", employee.getPhoneNumber());
        userData.put("userPositionId", employee.getPosition_id());
        userData.put("userStatusId", employee.getStatus_id());

        Date expDate = new Date(  System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .setIssuer("Fcu 113-1 CSIE Team 2")
                .setSubject(employee_id)
                .setClaims(userData)
                .setIssuedAt(new Date())
                .setExpiration(expDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 驗證 JWT
//    public boolean validateToken(String token, String username) {
//        String extractedUsername = extractUsername(token);
//        return (extractedUsername.equals(username) && !isTokenExpired(token));
//    }

//    // 提取用戶名
//    public String extractUsername(String token) {
//        return extractAllClaims(token).getSubject();
//    }
//
//    // 驗證 Token 是否過期
//    private boolean isTokenExpired(String token) {
//        return extractAllClaims(token).getExpiration().before(new Date());
//    }

    // 提取所有 Claims
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }

    // 獲取簽名的 Key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
    }
}
