package pe.edu.utp.prisma_api.infraestructure.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Value("${frontend.url}")
  private String frontendUrl;

  public void sendVerificationEmail(String email, String token) throws MessagingException {
    String link = frontendUrl + "/auth/verify-email?token=" + token;

    Context context = new Context();
    context.setVariable("link", link);

    String html = templateEngine.process("mail/verification", context);

    send(email, "Verifica tu cuenta", html);
  }

  public void sendInvitationEmail(String email, String teamName, String token) throws MessagingException {
    String link = frontendUrl + "/auth/invitations/accept?token=" + token;

    Context context = new Context();
    context.setVariable("teamName", teamName);
    context.setVariable("link", link);

    String html = templateEngine.process("mail/invitation", context);

    send(email, "Te invitaron a unirte a " + teamName, html);
  }

  public void sendPasswordResetEmail(String email, String token) throws MessagingException {
    String link = frontendUrl + "/auth/reset-password?token=" + token;

    Context context = new Context();
    context.setVariable("link", link);

    String html = templateEngine.process("mail/password-reset", context);

    send(email, "Restablece tu contraseña", html);
  }

  private void send(String to, String subject, String htmlBody) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setFrom(fromEmail);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlBody, true); // true = es HTML

    mailSender.send(message);
  }
}
