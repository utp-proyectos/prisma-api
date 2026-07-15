package pe.edu.utp.prisma_api.domain.team.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;

@Getter
@Setter
@AllArgsConstructor
public class TeamDetailResponse {
  private UUID id;
  private String name;
  private List<TeamMemberResponse> members;
  private List<ProjectResponse> projects;
  private TeamRole userRole;
}
