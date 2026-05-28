package pe.edu.utp.prisma_api.domain.folder.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;

@Data
public class FolderDetailDTO {
  private String id;
  private String nombre;
  private Boolean isPrivate;
  private String teamMemberId; // creador
  private String projectId;
  private List<BoardResponseDTO> boards; // pizarras dentro del folder
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
