package pe.edu.utp.prisma_api.domain.board.dto;

import lombok.Data;

@Data
public class BoardRequestDTO {
  private String name;
  private String description;
  private String folderId;
  private Boolean isPrivate;
}
