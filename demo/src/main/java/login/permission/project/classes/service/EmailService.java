package login.permission.project.classes.service;

import login.permission.project.classes.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender mailSender;

  /**
   *
   * 員工註冊
   * 驗證郵件方法
   */
  public void sendVerificationEmail(String to, String token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("wwwhow37@gamil.com"); // 設定寄件者
    message.setTo(to);
    message.setSubject("請驗證您的帳號");
    message.setText("親愛的員工，您好：\n\n"
            + "請點擊以下連結驗證您的帳號：\n"
            + "http://localhost:8085/employee/test/verify?token=" + token + "\n\n"
            + "此連結有效期為24小時，請儘速驗證。\n"
            + "如果您沒有申請帳號，請忽略此郵件。");

//    mailSender.send(message);
    try {
      mailSender.send(message);
    } catch (MailException e) {
      throw new RuntimeException("郵件發送失敗: " + e.getMessage());
    }
  }


  /**
   *
   * 登入系統
   * 通知郵件方法
   */
  public void sendLoginNotification(Employee employee) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("wwwhow37@gamil.com");
    message.setTo(employee.getEmail());

    String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    message.setSubject("系統登入通知 - " + currentTime);

    String content = String.format("""
            親愛的 %s：
            
            這是一封系統自動發送的登入通知。
            
            登入詳情：
            - 員工編號：%d
            - 登入時間：%s
            - 部門：%s
            - 職位：%s
            
            如果這不是您本人的登入操作，請立即聯繫系統管理員。
            
            祝您使用愉快！
            
            此為系統自動發送，請勿回覆。
            """,
            employee.getName(),
            employee.getEmployee_id(),
            currentTime,
            employee.getDepartment() != null ? employee.getDepartment().getDepartment_name() : "未設定部門",
            employee.getPosition() != null ? employee.getPosition().getPosition() : "未設定職位"
    );

    message.setText(content);

    try {
      mailSender.send(message);
    } catch (MailException e) {
      // 這裡我們可以只記錄錯誤而不拋出異常，因為這不應該影響登入流程
      System.err.println("登入通知郵件發送失敗: " + e.getMessage());
    }
  }


  /**
   *
   * 忘記密碼
   * 發送重置密碼郵件的方法
   */
  public void sendResetPasswordEmail(String to, String token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("wwwhow37@gamil.com");
    message.setTo(to);
    message.setSubject("密碼重置請求");
    message.setText("親愛的員工，您好：\n\n"
            + "我們收到了您的密碼重置請求。請點擊以下連結重置您的密碼：\n"
            + "http://localhost:5173/reset-password?token=" + token + "\n\n"
            + "此連結有效期為1小時，請儘速完成密碼重置。\n"
            + "如果這不是您發起的請求，請忽略此郵件。");

    try {
      mailSender.send(message);
    } catch (MailException e) {
      throw new RuntimeException("郵件發送失敗: " + e.getMessage());
    }
  }

}
