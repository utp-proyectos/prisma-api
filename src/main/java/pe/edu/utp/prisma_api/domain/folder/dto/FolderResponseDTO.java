package pe.edu.utp.prisma_api.domain.folder.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;

@Data
public class FolderResponseDTO {
  private String id;
  private String name;
  private Boolean isPrivate;
  private List<BoardResponseDTO> boards;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
