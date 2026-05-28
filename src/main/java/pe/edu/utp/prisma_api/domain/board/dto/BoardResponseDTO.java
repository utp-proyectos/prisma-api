package pe.edu.utp.prisma_api.domain.board.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardResponseDTO {
  private String id;
  private String name;
  private Boolean isPrivate;
  private String folderId;
  private String projectId;
  private String teamMemberId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
