package login.permission.project.classes.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerResponse {

    private String message;
    private Object data;

}
