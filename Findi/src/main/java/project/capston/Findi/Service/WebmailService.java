package project.capston.Findi.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class WebmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendRoommateMail(String to, String requesterName, String studentId,
                                 String department, String gender, String smoking,
                                 String mbti, String lifestyle, String email) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String subject = "Findi 룸메이트 매칭 알림";

        String html = getHtmlTemplate()
                .replace("${requesterName}", requesterName)
                .replace("${studentId}", studentId)
                .replace("${department}", department)
                .replace("${gender}", gender)
                .replace("${smoking}", smoking)
                .replace("${mbti}", mbti)
                .replace("${lifestyle}", lifestyle)
                .replace("${email}", email);


        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }

    private String getHtmlTemplate() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Findi 룸메이트 매칭 알림</title>
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #2c3e50;">Findi 룸메이트 매칭 알림</h2>
                    <p>안녕하세요,</p>
                    <p><strong>${requesterName}</strong>님이 당신을 룸메이트로 희망하였습니다.</p>
                    <h3>요청자 정보</h3>
                    <table style="width: 100%; border-collapse: collapse;">
                        <tr><td><strong>이름</strong></td><td>${requesterName}</td></tr>
                        <tr><td><strong>학번</strong></td><td>${studentId}</td></tr>
                        <tr><td><strong>학과</strong></td><td>${department}</td></tr>
                        <tr><td><strong>성별</strong></td><td>${gender}</td></tr>
                        <tr><td><strong>흡연 여부</strong></td><td>${smoking}</td></tr>
                        <tr><td><strong>MBTI</strong></td><td>${mbti}</td></tr>
                        <tr><td><strong>생활습관</strong></td><td>${lifestyle}</td></tr>
                    </table>
                    <p style="margin-top: 20px;">${requesterName}님의 이메일: ${email}</p>
                    <div style="margin-top: 30px; text-align: center;">
                        <a href="https://findi.example.com" style="background-color: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px;">Findi 바로가기</a>
                    </div>
                    <p style="font-size: 12px; color: #888; margin-top: 40px;">본 메일은 Findi 서비스의 룸메이트 매칭 요청을 기반으로 발송된 안내 메일입니다.</p>
                </div>
            </body>
            </html>
            """;
    }
}
