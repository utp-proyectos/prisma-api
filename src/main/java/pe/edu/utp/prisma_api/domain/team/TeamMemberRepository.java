package pe.edu.utp.prisma_api.domain.team;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {
  Optional<TeamMember> findByUserId(String userId);

  Optional<TeamMember> findByTeamId(String teamId);

  boolean existsByUserIdAndTeamId(String userId, String teamId);

  Optional<TeamMember> findByUserIdAndTeamId(String userId, String teamId);
}
