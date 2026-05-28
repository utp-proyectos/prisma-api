package pe.edu.utp.prisma_api.domain.folder.dto;

import lombok.Data;

@Data
public class FolderRequestDTO {
  private String nombre;
  private String teamMemberId;
  private Boolean isPrivate;
}
