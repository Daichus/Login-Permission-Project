package login.permission.project.classes.service;



import login.permission.project.classes.model.Status;
import login.permission.project.classes.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {

    @Autowired
    StatusRepository sr;

    public List<Status> getAllStatus() {
        return sr.findAll();
    }

    public String addStatus(Status status) {
        sr.save(status);
        return String.format("新增員工狀態成功\n狀態名稱: %s\n狀態id: %s",
                status.getName(),
                status.getStatus_id());
    }

    public String updateStatus (Status status) {
        if(status != null) {
            sr.save(status);
            return "修改員工狀態完成";
        } else {
            return "更新員工狀態失敗";
        }

    }

    public String deleteStatus (int id) {
        sr.deleteById(id);
        return "刪除員工狀態成功";
    }
}
