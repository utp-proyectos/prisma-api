package pe.edu.utp.prisma_api.domain.folder.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;

@Getter
@Setter
public class FolderDetailDTO {
  private UUID id;
  private String nombre;
  private Boolean isPrivate;
  private UUID projectId;
  private List<BoardResponseDTO> boards; // pizarras dentro del folder
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UUID creatorId;

}
