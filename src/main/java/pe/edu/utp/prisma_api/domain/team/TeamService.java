package pe.edu.utp.prisma_api.domain.team;

import java.util.List;
import java.util.Optional;

import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.domain.team.dto.TeamRequestDTO;
import pe.edu.utp.prisma_api.domain.team.dto.TeamResponseDTO;

public interface TeamService {
    List<TeamResponseDTO> getAll();

    TeamResponseDTO save(TeamRequestDTO dto, String userId);

    Optional<TeamResponseDTO> update(String id, TeamRequestDTO dto);

    void delete(String id);

    // Métodos específicos
    void updateMemberRole(String teamId, String adminId, String targetUserId, TeamRole newRole);

    List<TeamResponseDTO> getUserTeams(String userId);
}
