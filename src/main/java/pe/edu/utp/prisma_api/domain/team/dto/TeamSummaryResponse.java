package pe.edu.utp.prisma_api.domain.team.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamSummaryResponse {
  private UUID id;
  private String name;
}
