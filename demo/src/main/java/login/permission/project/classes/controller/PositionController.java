package login.permission.project.classes.controller;

import login.permission.project.classes.annotation.LogOperation;
import login.permission.project.classes.model.Position;

import login.permission.project.classes.model.dto.PositionDTO;
import login.permission.project.classes.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Position")
@CrossOrigin("http://localhost:5173/")
public class PositionController {

    @Autowired
    PositionService positionService;

    @LogOperation(module = "職位管理", operation = "查詢", description = "查詢所有職位")
    @GetMapping("/get")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

//    @GetMapping("/getAll")
//    public List<PositionDTO> getAllPositionsAndUnit() {
//        return ds.getAllPositions().stream().map(position -> new PositionDTO(
//                position.getPosition_id(),
//                position.getPosition(),
//                position.getUnit().getUnit_id(),
//                position.getUnit().getUnit_name()
//        )).collect(Collectors.toList());
//    }

//    @LogOperation(module = "職位管理", operation = "查詢", description = "搜尋職位")
//    @GetMapping("/search")
//    public List<Position> searchPositions(
//            @RequestParam(required = false) Integer unitId,
//            @RequestParam(required = false) Integer positionId) {
//        return positionService.getPositionById(unitId, positionId);
//    }

    @LogOperation(module = "職位管理", operation = "新增", description = "新增職位")
    @PostMapping("/add")
    public ResponseEntity<?> addPosition (@RequestBody PositionDTO dto) {
        return positionService.addPosition(dto);
    }

    @LogOperation(module = "職位管理", operation = "修改", description = "修改職位")
    @PutMapping("/edit")
    public ResponseEntity<?> updatePosition (@RequestBody PositionDTO dto) {
        return positionService.updatePosition(dto);
    }

    @LogOperation(module = "職位管理", operation = "刪除", description = "刪除職位")
    @DeleteMapping("/delete/{id}")
    public String deletePosition (@PathVariable int id) {
        return positionService.deletePosition(id);
    }
}
