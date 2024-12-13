package login.permission.project.classes.model.util;


import login.permission.project.classes.model.ServerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 此方法是將先前回傳Response的代碼獨立出,
 * 減少重複的代碼。
 * 可以根據要回傳成功或失敗的訊息,選擇要調用的ResponseEntity類型
 */
@Component
public class ResponseUtil {

    public static ResponseEntity<ServerResponse> createResponse(String message, Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(new ServerResponse(message, data));
    }

    public static ResponseEntity<?> success(String message, Object data) {
        return createResponse(message, data, HttpStatus.OK);
    }

    public static ResponseEntity<?> error(String message, HttpStatus status) {
        return createResponse(message, null, status);
    }
}
