package pe.edu.utp.prisma_api.domain.folder.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;

@Getter
@Setter
public class FolderResponseDTO {
  private UUID id;
  private String name;
  private Boolean isPrivate;
  private List<BoardResponseDTO> boards;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
