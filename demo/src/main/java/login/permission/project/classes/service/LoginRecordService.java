package login.permission.project.classes.service;

import jakarta.persistence.Column;
import login.permission.project.classes.model.LoginRecord;
import login.permission.project.classes.repository.LoginRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoginRecordService {

    @Autowired
    LoginRecordRepository dr;

    public List<LoginRecord> getAllLoginRecords() {
        return dr.findAll();
    }

    public String addLoginRecord(LoginRecord loginRecord) {
        dr.save(loginRecord);
        return String.format("新增登入記錄成功\n登入代號: %s\n員工代號: %s\n登入時間: %s\n登出時間: %s\nip位址: %s\n狀態: %s",
                loginRecord.getRecord_id(),
                loginRecord.getEmployee_id(),
                loginRecord.getLogin_time(),
                loginRecord.getLogout_time(),
                loginRecord.getIp_address(),
                loginRecord.getStatus());

    }

    //登入代號	員工代號	登入時間	登出時間	ip位址	狀態(成功或失敗)

    public String updateLoginRecord (LoginRecord empPositionMap) {
        if(empPositionMap != null) {
            dr.save(empPositionMap);
            return "修改登入記錄資訊完成";
        } else {
            return "更新登入記錄資訊失敗";
        }
    }

    public String deleteLoginRecord (int id) {
        dr.deleteById(id);
        return "刪除登入記錄成功";
    }
}
