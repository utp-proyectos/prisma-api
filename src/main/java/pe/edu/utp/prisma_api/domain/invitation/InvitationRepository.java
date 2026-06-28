package pe.edu.utp.prisma_api.domain.invitation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
  Optional<Invitation> findByToken(UUID token);

  Optional<Invitation> findByEmailAndTeamId(String email, UUID teamId);
}
