package pe.edu.utp.prisma_api.domain.team;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {
  boolean existsByUserIdAndTeamId(UUID userId, UUID teamId);

  Optional<TeamMember> findByTeamId(UUID teamId);

  Optional<TeamMember> findByUserIdAndTeamId(UUID userId, UUID teamId);
}
