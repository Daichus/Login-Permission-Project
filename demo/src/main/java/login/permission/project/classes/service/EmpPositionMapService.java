package login.permission.project.classes.service;

import login.permission.project.classes.model.EmpPositionMap;
import login.permission.project.classes.repository.EmpPositionMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmpPositionMapService {

    @Autowired
    EmpPositionMapRepository dr;

    public List<EmpPositionMap> getAllEmpPositionMaps() {
        return dr.findAll();
    }

    public String addEmpPositionMap(EmpPositionMap empPositionMap) {
        dr.save(empPositionMap);
        return String.format("新增職位設定成功\n職位設定代號: %s\n員工代號: %s\n職位代號: %s",
                empPositionMap.getEmpPositionMap_id(),
                empPositionMap.getEmployee_id(),
                empPositionMap.getPosition_id());
    }

    public String updateEmpPositionMap (EmpPositionMap empPositionMap) {
        if(empPositionMap != null) {
            dr.save(empPositionMap);
            return "修改職位設定資訊完成";
        } else {
            return "更新職位設定資訊失敗";
        }
    }

    public String deleteEmpPositionMap (int id) {
        dr.deleteById(id);
        return "刪除職位設定成功";
    }

}




