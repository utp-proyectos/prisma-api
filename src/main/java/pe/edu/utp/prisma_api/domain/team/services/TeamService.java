package pe.edu.utp.prisma_api.domain.team.services;

import java.util.List;
import java.util.Optional;

import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.domain.team.dto.CreateTeamDTO;
import pe.edu.utp.prisma_api.domain.team.dto.TeamDTO;
import pe.edu.utp.prisma_api.domain.team.dto.UpdateTeamDTO;

public interface TeamService {

    List<TeamDTO> findAll();

    Optional<TeamDTO> findById(String id);

    TeamDTO save(CreateTeamDTO dto, String ownerId);

    Optional<TeamDTO> update(String id, UpdateTeamDTO dto);

    void delete(String id);

    List<TeamDTO> findByUser(String userId);

    void updateMemberRole(
            String teamId,
            String adminId,
            String targetUserId,
            TeamRole role);
}