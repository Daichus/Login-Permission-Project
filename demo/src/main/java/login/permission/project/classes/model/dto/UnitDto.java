package login.permission.project.classes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnitDto {

    private int unit_id;

    private String unit_name;

    private String unit_code;

    private Integer department_id;
}
