package pe.edu.utp.prisma_api.domain.team;

import java.util.List;
import java.util.Optional;

import pe.edu.utp.prisma_api.domain.team.dto.TeamRequestDTO;
import pe.edu.utp.prisma_api.domain.team.dto.TeamResponseDTO;

public interface TeamService {
    List<TeamResponseDTO> getAll();

    TeamResponseDTO save(TeamRequestDTO dto);

    Optional<TeamResponseDTO> update(String id, TeamRequestDTO dto);

    void delete(String id);

    // Métodos específicos que el genérico no te dejaba tener cómodamente
    // void addMember(String teamId, AddMemberRequestDTO dto);

    // List<TeamResponseDTO> getUserTeams(String userId);

}
