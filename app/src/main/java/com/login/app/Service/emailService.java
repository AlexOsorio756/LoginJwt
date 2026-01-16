package com.login.app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class emailService {

    @Autowired
    private JavaMailSender mailSender;
    //Para este paso de enviar el correo lo simulamos con una pagina llamada mailtrap que captura los correos enviados
    public void sendMailConfirm(String toSend, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toSend);
        message.setSubject("Confirm your account - App 2026");
        message.setText("Hello, click here to create your password: \n" +
                "http://localhost:8080/auth/confirm.html?token=" + token);

        mailSender.send(message);
    }

    public void sendEmailForgotPassword(String toSend, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toSend);
        message.setSubject("Reset Your Password - App 2026");
        message.setText("Hello, click here to reset your password: \n" +
                "http://localhost:8080/auth/reset-password.html?token=" + token);
        mailSender.send(message);
    }
}