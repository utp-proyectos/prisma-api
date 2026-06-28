package pe.edu.utp.prisma_api.domain.board.dto;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDTO {
  private String name;
  private String description;
  private UUID folderId;
  private Boolean isPrivate;
}
