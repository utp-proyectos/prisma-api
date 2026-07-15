package pe.edu.utp.prisma_api.domain.folder.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFolderDTO {
  private UUID folderId;
  private UUID teamId;
  private UUID projectId;
  private String name;
}
