package login.permission.project.classes.model;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
