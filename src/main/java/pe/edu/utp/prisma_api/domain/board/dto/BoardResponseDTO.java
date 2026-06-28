package pe.edu.utp.prisma_api.domain.board.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BoardResponseDTO {
  private UUID id;
  private String name;
  private String description;
  private Boolean isPrivate;
  private String thumbnailUrl;
  private UUID folderId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
