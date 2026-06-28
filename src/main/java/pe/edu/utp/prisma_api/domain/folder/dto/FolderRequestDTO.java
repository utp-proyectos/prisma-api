package pe.edu.utp.prisma_api.domain.folder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderRequestDTO {
  private String name;
  private Boolean isPrivate;
}
