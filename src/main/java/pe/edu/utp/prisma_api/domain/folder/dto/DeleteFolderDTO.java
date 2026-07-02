package pe.edu.utp.prisma_api.domain.folder.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteFolderDTO {
  private UUID folderId;
  private UUID projectId;
  private UUID teamId;
}
