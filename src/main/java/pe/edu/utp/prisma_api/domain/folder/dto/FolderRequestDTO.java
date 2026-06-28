package pe.edu.utp.prisma_api.domain.folder.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class FolderRequestDTO {
  private String nombre;
  private UUID teamMemberId;
  private Boolean isPrivate;
}
