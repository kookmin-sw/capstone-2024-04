package com.drm.server.service;

import com.drm.server.common.enums.ErrorCode;
import com.drm.server.exception.BusinessLogicException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSender;  // 의존성 주입을 통해 필요한 객체를 가져옴
    public void sendEmail(String toEmail,
                          String title,
                          String text)  {
//        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        MimeMessage message = createMail(toEmail, text);
        try {
            javaMailSender.send(message);
        } catch (RuntimeException e) {
            log.error("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw  new RuntimeException(e.getMessage());
        }
    }


    private SimpleMailMessage createEmailForm(String toEmail, String title, String text){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
    public MimeMessage createMail(String mail,String text){
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom("이메일");   // 보내는 이메일
            message.setRecipients(MimeMessage.RecipientType.TO, mail); // 보낼 이메일 설정
            message.setSubject("[Do you Read Me] 회원가입을 위한 이메일 인증");  // 제목 설정
            String body = getBody(text);
            message.setText(body,"UTF-8", "html");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }

    private static String getBody(String number) {
        String body = "";
        body += "<h1>" + "안녕하세요." + "</h1>";
        body += "<h1>" + "Do you Read Me 입니다." + "</h1>";
        body += "<h3>" + "회원가입을 위한 요청하신 인증 번호입니다." + "</h3><br>";
        body += "<h2>" + "아래 코드를 회원가입 창으로 돌아가 입력해주세요." + "</h2>";

        body += "<div align='center' style='border:1px solid black; font-family:verdana;'>";
        body += "<h2>" + "회원가입 인증 코드입니다." + "</h2>";
        body += "<h1 style='color:blue'>" + number + "</h1>";
        body += "</div><br>";
        body += "<h3>" + "감사합니다." + "</h3>";
        return body;
    }
}
