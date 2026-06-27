package pe.edu.utp.prisma_api.domain.team.dto;

import java.time.LocalDate;

import lombok.Data;
import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.domain.user.dto.UserResponseDTO;

@Data
public class TeamMemberDTO {

    private String id;

    private UserResponseDTO user;

    private TeamRole role;

    private LocalDate joinedAt;
}