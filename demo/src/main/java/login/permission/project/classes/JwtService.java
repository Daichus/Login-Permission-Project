package login.permission.project.classes;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;


/**
 * JwtService 類負責處理與 JWT Token相關的操作 。

 * 本類主要功能包括：
 * 1. 生成 JWT Token，包含自定義的 Payload。
 * 2. 驗證前端請求中的 JWT Token，並提取 Payload 部分。
 * 3. 提供簽名密鑰生成方法，確保 JWT Token 的安全性。

 * 使用場景：
 * - 生成 Token 時，後端可將必要的使用者資訊（如登錄紀錄 ID、權限碼等）封裝至 Token 的 Payload，並返回給前端。
 * - 驗證 Token 時，後端可檢查 Token 的合法性及其內容，並提取相關的使用者資訊進行後續操作。

 * 注意：
 * - 簽名密鑰（KEY）僅能由後端擁有，不會暴露給前端或第三方。
 * - Token 的過期時間由 EXPIRATION_TIME 定義，需根據應用場景調整。
 */
@Component
public class JwtService {

    /**
     * 用途：加密用字串
     */
    private static final String KEY = "5gk4g4j5i32k7ru8auu4m4ul4t6su3ah9j14i2l4g45k4g4bu4ke7rul4kmpv4cl391j4cl3";

    /**
     * 用途：設定Token有效時間, 以毫秒為單位,每小時使用者需重新登錄
     */
    private static final long EXPIRATION_TIME = 3_600_000;

    /**
     * 用途：產生JWT Token
     * 說明：
     * 1.JWT包含Header,Payload,Signature三個部分
     * 2.方法中需要手動設定JWT的Payload以定義要傳送回前端的訊息
     */
    public String generateToken(String employeeId,int recordId) {


            Map<String, Object> userData = new HashMap<>();
            userData.put("loginRecordId",recordId);
            userData.put("permissionCode",new String[]{});

            Date expDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

            return Jwts.builder()
                    .setClaims(userData)
                    .setIssuer("Fcu 113-1 CSIE Team 2")
                    .setSubject(employeeId )
                    .setIssuedAt(new Date())
                    .setExpiration(expDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();


    }

    // 獲取簽名的 Key,Key僅為後端所擁有
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
    }

    /**
     * 抽取使用者傳過來的
     * token中的payload部分並進行驗證
     * 並還原成Claims物件
     */
    public Claims verifyToken(HttpServletRequest request) {
        Claims claims;
        try{
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return null;
            }
            String token = authHeader.substring(7);
            claims =  Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .requireIssuer("Fcu 113-1 CSIE Team 2")
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch(JwtException e) {
            claims =  null;
        }
        return claims;
    }







}



