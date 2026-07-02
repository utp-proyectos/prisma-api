package pe.edu.utp.prisma_api.domain.board.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveBoardDTO {
  private UUID boardId;
  private UUID projectId;
  private UUID folderId; // null si es removeFromFolder
  private UUID teamId;

}