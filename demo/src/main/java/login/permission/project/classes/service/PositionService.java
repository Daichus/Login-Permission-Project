package login.permission.project.classes.service;

import login.permission.project.classes.model.Position;
import login.permission.project.classes.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    PositionRepository pr;

    public List<Position> getAllPositions() { return pr.findAll(); }

    public String addPosition(Position position){
        pr.save(position);
        return String.format("新增職位成功\n職位名稱: %s\n職位id: %s\n科別代號: %s",
                position.getPosition(),
                position.getPosition_id(),
                position.getUnit_id());
    }

    public List<Position> findAll(){ return pr.findAll(); }

    public String updatePosition(Position position) {
        if (position != null) {
            pr.save(position);
            return "修改職位資訊完成";
        } else {
            return "更新職位資訊失敗";
        }
    }

    public String deletePosition(int id) {
        pr.deleteById(id);
        return "刪除職位成功";
    }
}
