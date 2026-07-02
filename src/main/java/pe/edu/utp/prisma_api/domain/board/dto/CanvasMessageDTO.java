package pe.edu.utp.prisma_api.domain.board.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CanvasMessageDTO {
  private UUID teamId;
  private UUID boardId;
  private String type; // CREATE, UPDATE, DELETE, BATCH
  private Object shape;
  private List<String> names; // para batch update
}
