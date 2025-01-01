package login.permission.project.classes;

import io.jsonwebtoken.Claims;

import jakarta.servlet.http.HttpServletRequest;

//import login.permission.project.classes.security.JwtAuthFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    //    static JwtAuthFilter filter;
    static JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    //測試jwt基礎解析的功能
    //透過mockito模擬http請求
    //檢查檢析後的claims是否與指定的參數相符
    @Test
    @DisplayName("測試Jwt基礎解析")
    void testParseJwtToken () {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token  = jwtService.generateToken("1", 1);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        Claims claims = jwtService.isTokenValid(request);

        assertEquals("1", claims.getSubject());
        assertEquals(1,claims.get("loginRecordId"));
        assertEquals("Fcu 113-1 CSIE Team 2", claims.getIssuer());

        String token2  = jwtService.generateToken("101", 101);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token2);
        Claims claims2 = jwtService.isTokenValid(request);
        assertEquals("101", claims2.getSubject());
        assertEquals(101,claims2.get("loginRecordId"));
        assertEquals("Fcu 113-1 CSIE Team 2", claims.getIssuer());
    }

    //測試接收到無效的Jwt token時的情況
    //無效的token經解析後應為null
    //此為測試不符合格式的token的情形
    @Test
    @DisplayName("測試Jwt解析無效的Jwt token")
    void testInvalidJwtToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer 1234567890abc");
        Claims claims = jwtService.isTokenValid(request);

        assertNull(claims);
    }

    //此為測試header為null的情形
    //同樣claims應為null
    @Test
    @DisplayName("測試Jwt解析無效的Jwt token, header為null")
    void testInvalidJwtToken_nullHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);
        Claims claims = jwtService.isTokenValid(request);

        assertNull(claims);
    }

    //此為測試header格式無效的情形
    @Test
    @DisplayName("測試Jwt解析無效的Jwt token, header未包含Bearer字串")
    void testInvalidJwtToken_invalidHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Hello World");
        Claims claims = jwtService.isTokenValid(request);

        assertNull(claims);
    }

    // 測試對樣: 解析token時遇到 Bearer 前綴格式錯誤的情況
    @Test
    @DisplayName("測試Jwt解析無效的Jwt token, header Bearer字串格式錯誤")
    void testIsTokenValid_withMalformedBearerPrefix() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bear er");

        Claims claims = jwtService.isTokenValid(request);
        assertNull(claims);
    }

    //測試對樣: generateToken方法在員工id不同情況下的執行情況
    //此為測試不符格式的id格式的情形
    @Test
    @DisplayName("測試Jwt解析無效的Jwt token, 員工id格式錯誤的情形")
    void testGenerateToken_EmployeeId_value_noneNumeric() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jwtService.generateToken("2a0", 101)
        );
        assertEquals("產生token失敗: 員工id必須是整數字串或不得有負號，實際輸入為: 2a0", exception.getMessage());

        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> jwtService.generateToken("abc", 101)
        );
        assertEquals("產生token失敗: 員工id必須是整數字串或不得有負號，實際輸入為: abc", exception2.getMessage());

        IllegalArgumentException exception3 = assertThrows(
                IllegalArgumentException.class,
                () -> jwtService.generateToken("#1", 101)
        );
        assertEquals("產生token失敗: 員工id必須是整數字串或不得有負號，實際輸入為: #1", exception3.getMessage());

    }

    //測試對樣:  員工id為負數字串的情形
    @Test
    @DisplayName("測試Jwt解析無效的Jwt token, 員工id為負數字串的情形")
    void testGenerateToken_EmployeeId_value_negativeValue () {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jwtService.generateToken("-1", 101)
        );
        assertEquals("產生token失敗: 員工id必須是整數字串或不得有負號，實際輸入為: -1", exception.getMessage());
    }


    //  測試對樣: GenerateToken在不同的record_id輸入值時的產生結果
    //---------------------------------------------
    //#此方法測是參數符合條件的情況
    @Test
    @DisplayName("測試Jwt解析Jwt token, 登錄紀錄id符合標準的情形")
    void testGenerateToken_record_id_value(){
        String token2 = jwtService.generateToken("1",0);
        String token3 = jwtService.generateToken("1",1);
        String token4 = jwtService.generateToken("1",101);
        String token5 = jwtService.generateToken("1",Integer.MAX_VALUE);
        String token6 = jwtService.generateToken("1",Integer.MAX_VALUE - 1);
        assertNotNull(token2);
        assertNotNull(token3);
        assertNotNull(token4);
        assertNotNull(token5);
        assertNotNull(token6);
    }




    //  測試對樣: GenerateToken在不同的record_id輸入值時的產生結果
    //---------------------------------------------
    //#此方法測是參數不符合條件的情況
    @Test
    @DisplayName("測試Jwt解析無效的Jwt token, 登錄紀錄id為負數的情形")
    void testGenerateToken_record_id_value_negativeValue(){
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jwtService.generateToken("1", -1)
        );
        assertEquals("登錄記錄ID不得為負，實際輸入為: -1", exception.getMessage());
    }

}