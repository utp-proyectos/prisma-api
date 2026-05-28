package pe.edu.utp.prisma_api.domain.board.dto;

import lombok.Data;

@Data
public class BoardRequestDTO {
  private String name;
  private String folderId; // opcional, null si se crea en la raíz
  private String teamMemberId;
  private String konvaData; // null en create, usado en update (auto-guardado)
}
