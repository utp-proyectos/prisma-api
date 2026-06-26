package pe.edu.utp.prisma_api.domain.EmailVerification;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {
  Optional<EmailVerification> findByToken(String token);

  Optional<EmailVerification> findByUserId(String userId);
}
