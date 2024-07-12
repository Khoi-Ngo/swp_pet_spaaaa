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
import java.util.UUID;

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
    public String send(String email) throws MessagingException  {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("PET SPA PLACE - Đặt lại mật khẩu");
        String link = createSendTokenLink(email);
        String content = "<html>"
                + "<body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #007bff;'>Thay đổi mật khẩu</h2>"
                + "<p>Truy cập vào đường dẫn sau để tiến hành thay đổi mật khẩu của bạn :</p>"
                + "<p><a href='" + link + "' style='padding: 10px 20px; background-color: #007bff; color: #fff; text-decoration: none; border-radius: 5px;'>Thay đổi mật khẩu</a></p>"
                + "<p>Lưu ý: Mỗi đường dẫn chỉ tồn tại trong thời gian 5 phút.</p>"
                + "</body>"
                + "</html>";
        helper.setText(content, true);
        emailSender.send(message);
        return "Send confirm link successfully! "+link;
    }

    public String createSendTokenLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email " + email + " does not exist"));

        String token = UUID.randomUUID().toString();
        System.out.println("token: " + token);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDateTime(LocalDateTime.now().plusMinutes(5));

        tokenRepository.save(passwordResetToken);
        // Generate the reset password link
//        String link = "http://localhost:8080/api/v1/auth/password/changePassword?token=" + token;
        String link = "https://philfodennn.up.railway.app/api/v1/auth/password/changePassword?token=" + token;
        return link;
    }

    public Object changePassword(String token, PasswordChangeRequest request) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token);
        if (passwordResetToken == null || passwordResetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            return "Invalid or expired token.";
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "New password and confirm password do not match";
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
//        Optionally, delete the token after use
//        passwordResetToken.setDeleted(true);
//        tokenRepository.save(passwordResetToken);
        return "Password has been changed successfully";
    }

    public Object handleForgotPassword(Map<String, String> request) throws MessagingException {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            throw new IllegalStateException("Email is required");
        }
        return send(email);
    }

    public Object validateResetToken(String token) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token);
        if (passwordResetToken == null || passwordResetToken.getExpiryDateTime().isBefore(LocalDateTime.now())) {
            return "Invalid or expired token.";
        }
        return "Valid token.";
    }

}