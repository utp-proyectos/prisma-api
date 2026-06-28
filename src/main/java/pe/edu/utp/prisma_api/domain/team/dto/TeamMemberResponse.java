package pe.edu.utp.prisma_api.domain.team.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pe.edu.utp.prisma_api.common.enums.TeamRole;

@Getter
@Setter
@AllArgsConstructor
public class TeamMemberResponse {
  private UUID id;
  private String name;
  private String lastName;
  private String email;
  private String username;
  private String avatar;
  private TeamRole role;
}
