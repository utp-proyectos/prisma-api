package pe.edu.utp.prisma_api.infraestructure.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
  private final SpringTemplateEngine templateEngine;
  private final Gmail gmailService;

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Value("${frontend.url}")
  private String frontendUrl;

  public void sendVerificationEmail(String email, UUID token) throws MessagingException {
    String link = frontendUrl + "/auth/verify-email?token=" + token;

    Context context = new Context();
    context.setVariable("link", link);

    String html = templateEngine.process("mail/verification", context);

    send(email, "Verifica tu cuenta", html);
  }

  public void sendInvitationEmail(String email, String teamName, UUID token) throws MessagingException {
    String link = frontendUrl + "/invitations/accept?token=" + token;

    Context context = new Context();
    context.setVariable("teamName", teamName);
    context.setVariable("link", link);

    String html = templateEngine.process("mail/invitation", context);

    send(email, "Te invitaron a unirte a " + teamName, html);
  }

  public void sendPasswordResetEmail(String email, UUID token) throws MessagingException {
    String link = frontendUrl + "/auth/reset-password?token=" + token;

    Context context = new Context();
    context.setVariable("link", link);

    String html = templateEngine.process("mail/password-reset", context);

    send(email, "Restablece tu contraseña", html);
  }

  private void send(String to, String subject, String htmlBody) throws MessagingException {
    Session session = Session.getDefaultInstance(new Properties(), null);
    MimeMessage message = new MimeMessage(session);
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setFrom(fromEmail);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlBody, true);

    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      message.writeTo(buffer);
      byte[] rawMessageBytes = buffer.toByteArray();

      String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

      Message gmailMessage = new Message();
      gmailMessage.setRaw(encodedEmail);

      gmailService.users().messages().send("me", gmailMessage).execute();

    } catch (IOException e) {
      throw new MessagingException("Error al enviar el correo con la API de Gmail", e);
    }
  }
}
