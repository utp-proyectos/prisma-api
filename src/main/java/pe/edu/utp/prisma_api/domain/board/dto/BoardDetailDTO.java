package pe.edu.utp.prisma_api.domain.board.dto;

import lombok.Data;

@Data
public class BoardDetailDTO {
  private String id;
  private Boolean isPrivate;
  private String folderId;
}
