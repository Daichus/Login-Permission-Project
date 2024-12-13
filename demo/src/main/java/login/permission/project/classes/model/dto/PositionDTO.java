package login.permission.project.classes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionDTO {
    private int position_id;
    private String position;
    private int unit_id;
    private String unit_name;
}
