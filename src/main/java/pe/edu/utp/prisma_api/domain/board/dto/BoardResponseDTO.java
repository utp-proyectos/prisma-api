package pe.edu.utp.prisma_api.domain.board.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardResponseDTO {
  private String id;
  private String name;
  private String description;
  private Boolean isPrivate;
  private String thumbnailUrl;
  private String folderId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
