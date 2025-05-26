package com.playdata.userservice.user.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

 private final JavaMailSender mailSender;
     public String joinMail(String email) throws MessagingException {
         int authNum = makeRandomNumber();
         String setFrom = "luo199829@gmail.com";
         String toMail = email;
         String title = "order service";
         String content = "홈페이지 가입을 신청해 주셔서 감사합니다." +
                 "<br><br>" +
                 "인증 번호는 <strong>" + authNum + "</strong> 입니다. <br>" +
                 "해당 인증 번호를 인증번호 확인란에 기입해 주세요."; // 이메일에 삽입할 내용 (더 꾸며보세요)

         mailSender(setFrom, toMail, title, content);

      return Integer.toString(authNum);
     }

    private void mailSender(String setFrom, String toMail, String title, String content) throws MessagingException {
   MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message,false ,"utf-8");
     helper.setFrom(setFrom);
     helper.setTo(toMail);
     helper.setSubject(title);
     helper.setText(content, true);
     mailSender.send(message);

     }

    private int makeRandomNumber() {
         int checkNum = (int) (Math.random() * 100);

         return checkNum;

     }
}
