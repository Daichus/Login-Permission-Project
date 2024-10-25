package login.permission.project.classes.controller;

import login.permission.project.classes.model.Position;
import login.permission.project.classes.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Position")
public class PositionController {

    @Autowired
    PositionService ds;

    @GetMapping("/get")
    public List<Position> getAllPositions() {
        return ds.getAllPositions();
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
