package pe.edu.utp.prisma_api.infraestructure.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String fromEmail;

  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendVerificationEmail(String email, String token) {
    SimpleMailMessage mensaje = new SimpleMailMessage();
    mensaje.setFrom(email);
    mensaje.setTo(email);
    mensaje.setSubject("prueba");
    mensaje.setText(token);
    mailSender.send(mensaje);
  }
}
