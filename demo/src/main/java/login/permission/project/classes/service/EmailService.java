package login.permission.project.classes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender mailSender;

  public void sendVerificationEmail(String to, String token) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("d1204493@o365.fcu.edu.tw");// 設定寄件者
    message.setTo(to);
    message.setSubject("請驗證您的帳號");
    message.setText("親愛的員工，您好：\n\n"
            + "請點擊以下連結驗證您的帳號：\n"
            + "http://localhost:8080/employee/test/verify?token=" + token + "\n\n"
            + "此連結有效期為24小時，請儘速驗證。\n"
            + "如果您沒有申請帳號，請忽略此郵件。");

    mailSender.send(message);
  }

}
