package pe.edu.utp.prisma_api.domain.folder.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FolderResponseDTO {
  private String id;
  private String nombre;
  private Boolean isPrivate;
  private String teamMemberId; // creador
  private String projectId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
