package pe.edu.utp.prisma_api.domain.project.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectResponse {
  private UUID id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
}
