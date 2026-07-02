package pe.edu.utp.prisma_api.domain.board.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBoardDTO {
  private UUID boardId;
  private UUID projectId;
  private String name;
  private String description;
  private Boolean isPrivate;
  private UUID folderId;
  private UUID teamId;
}