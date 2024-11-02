package login.permission.project.classes.controller;

import login.permission.project.classes.model.Position;
import login.permission.project.classes.model.PositionDTO;
import login.permission.project.classes.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Position")
@CrossOrigin("http://localhost:5173/")
public class PositionController {

    @Autowired
    PositionService ds;

    @GetMapping("/get")
    public List<Position> getAllPositions() {
        return ds.getAllPositions();
    }

    @GetMapping("/getAll")
    public List<PositionDTO> getAllPositionsAndUnit() {
        return ds.getAllPositions().stream().map(position -> new PositionDTO(
                position.getPosition_id(),
                position.getPosition(),
                position.getUnit().getUnit_id(),
                position.getUnit().getUnit_name()
        )).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<Position> searchPositions(
            @RequestParam(required = false) Integer unitId,
            @RequestParam(required = false) Integer positionId) {
        return ds.getPositionById(unitId, positionId);
    }

    @PostMapping("/add")
    public String addPosition (@RequestBody Position position) {
        return ds.addPosition(position);
    }

    @PutMapping("/edit")
    public String updatePosition (@RequestBody Position position) {
        return ds.updatePosition(position);
    }

    @DeleteMapping("/delete/{id}")
    public String deletePosition (@PathVariable int id) {
        return ds.deletePosition(id);
    }
}
