package login.permission.project.classes;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import login.permission.project.classes.model.Employee;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class JwtService {

  /**
   * 加密字串,僅後端持有
   */
  private static final String KEY = "5gk4g4j5i32k7ru8auu4m4ul4t6su3ah9j14i2l4g45k4g4bu4ke7rul4kmpv4cl391j4cl3";

  /**
   * 以毫秒為單位,每小時使用者需重新登錄
   */
  private static final long EXPIRATION_TIME = 3_600_000;

  /**
   * 用於生成JWT Token的方法
   *JWT包含Header,Payload,Signature三個部分
   *方法中需要手動設定JWT的Payload以定義要傳送回前端的訊息
   */
  public String generateToken(Employee employee, int record_id, List<String> permission_code) {
    Map<String, Object> userData = new HashMap<>();
    String employee_id = String.valueOf(employee.getEmployee_id());
    userData.put("loginRecordId", record_id);
    userData.put("userName", employee.getName());
    userData.put("userEmail", employee.getEmail());
    userData.put("userPhone", employee.getPhoneNumber());
    userData.put("userStatusId", employee.getStatus_id());
    userData.put("permissionCode", permission_code);

    Date expDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

    return Jwts.builder()
            .setClaims(userData)
            .setIssuer("Fcu 113-1 CSIE Team 2")
            .setSubject(employee_id)
            .setIssuedAt(new Date())
            .setExpiration(expDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
  }


  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
  }

  /**
   * 抽取使用者傳過來的
   * token中的payload部分並進行驗證
   * 並還原成Claims物件
   */
  public Claims isTokenValid(HttpServletRequest request) {
    try {
      String token = request.getHeader("Authorization").replace("Bearer ", "");
      return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .requireIssuer("Fcu 113-1 CSIE Team 2")
                .build()
                .parseClaimsJws(token)
                .getBody();
    } catch (JwtException e) {
      System.out.println("Token validation failed: " + e.getMessage());
      return null;
    }
  }




}



