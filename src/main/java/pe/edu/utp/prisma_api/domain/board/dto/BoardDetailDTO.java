package pe.edu.utp.prisma_api.domain.board.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDetailDTO {
  private UUID id;
  private Boolean isPrivate;
  private String folderId;
}
