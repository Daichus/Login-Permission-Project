package login.permission.project.classes.service;

import login.permission.project.classes.model.Position;
import login.permission.project.classes.model.Unit;
import login.permission.project.classes.model.dto.PositionDTO;
import login.permission.project.classes.model.util.ResponseUtil;
import login.permission.project.classes.repository.PositionRepository;
import login.permission.project.classes.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    UnitRepository unitRepository;

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

//    public List<Position> getPositionById(Integer unitId, Integer positionId) {
//        return positionRepository.findByUnitIdAndPositionId(unitId, positionId);
//    }

    public ResponseEntity<?> addPosition(PositionDTO dto){
        Optional<Unit> unitOptional = unitRepository.findById(dto.getUnit_id());
        Position position = new Position();
        position.setPosition(dto.getPosition());
        unitOptional.ifPresent(position::setUnit);
        positionRepository.save(position);
        return ResponseUtil.success("新增職位成功",null);
    }

    public ResponseEntity<?> updatePosition(PositionDTO dto) {
        Position position = positionRepository.findById(dto.getPosition_id()).get();
        position.setPosition(dto.getPosition());
        position.setUnit(unitRepository.findById(dto.getUnit_id()).get());
        positionRepository.save(position);
        return ResponseUtil.success("更新 Position 成功", position);
    }

    public String deletePosition(int id) {
        positionRepository.deleteById(id);
        return "刪除職位成功";
    }
}
