package pe.edu.utp.prisma_api.domain.folder.dto;

import lombok.Data;

@Data
public class FolderRequestDTO {
  private String name;
  private Boolean isPrivate;
}
