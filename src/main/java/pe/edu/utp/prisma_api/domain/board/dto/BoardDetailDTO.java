package pe.edu.utp.prisma_api.domain.board.dto;

import java.time.LocalDateTime;

public class BoardDetailDTO {
  private String id;
  private String name;
  private Boolean isPrivate;
  private String folderId;
  private String projectId;
  private String teamMemberId;
  private String konvaData; // único campo extra vs BoardResponseDTO
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
