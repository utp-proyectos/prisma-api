package pe.edu.utp.prisma_api.domain.team;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    List<Team> findByMembersUserId(String userId);
}
// Busca todos los equipos donde en su lista de 'members', exista un registro
// con ese 'userId'