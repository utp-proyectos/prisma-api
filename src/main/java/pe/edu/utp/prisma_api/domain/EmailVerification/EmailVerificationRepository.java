package pe.edu.utp.prisma_api.domain.EmailVerification;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
  Optional<EmailVerification> findByToken(UUID token);

  Optional<EmailVerification> findByUserId(UUID userId);
}
