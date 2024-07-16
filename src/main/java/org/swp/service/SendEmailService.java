package org.swp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swp.Email.EmailSender;
import org.swp.dto.request.PasswordChangeRequest;
import org.swp.entity.User;
import org.swp.entity.other.PasswordResetToken;
import org.swp.repository.IUserRepository;
import org.swp.repository.ITokenRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class SendEmailService implements EmailSender {

    @Autowired
    JavaMailSender emailSender;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ITokenRepository tokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public String send(String email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("PET SPA PLACE - Đặt lại mật khẩu");
        String token = createSendTokenLink(email);
        String content = "<html>"
                + "<body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #007bff;'>Thay đổi mật khẩu</h2>"
                + "<p style='color: black;'>Mã xác nhận đặt lại mật khẩu của bạn là: <span style='font-size: 20px; font-weight: bold;'>" + token + "</span></p>"
                + "<p style='color: black;'>Nhập mã sau vào trang web để tiến hành đặt lại mật khẩu của bạn :</p>"
                + "<p style='color: black; font-style: italic; font-size: smaller;'>Lưu ý: Mỗi mã token chỉ tồn tại trong thời gian 5 phút.</p>"
                + "</body>"
                + "</html>";
        helper.setText(content, true);
        emailSender.send(message);
        return "Email was sent with token: " + token;
    }


    public String createSendTokenLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email " + email + " does not exist"));
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteAllExpiredSince(now);

        Random random = new Random();
        int number = random.nextInt(900000)+100000;
        String token = String.valueOf(number);
        System.out.println("token: " + token);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDateTime(LocalDateTime.now().plusMinutes(5));

        tokenRepository.save(passwordResetToken);
        // Generate the reset password link
//        String link = "http://localhost:8080/api/v1/auth/password/checkValidate?token=" + token;
        return token;
    }

    public Object changePassword(PasswordChangeRequest request) {
        String token = request.getToken();
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token."));

        if (passwordResetToken == null || passwordResetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException ("Invalid or expired token.");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
//       Delete the token after use
        tokenRepository.delete(passwordResetToken);
        return "Password has been changed successfully";
    }

    public Object handleForgotPassword(Map<String, String> request) throws MessagingException {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            throw new IllegalStateException("Email is required");
        }
        return send(email);
    }

    public boolean checkToken(String token) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token."));;

        if (passwordResetToken == null || passwordResetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

}