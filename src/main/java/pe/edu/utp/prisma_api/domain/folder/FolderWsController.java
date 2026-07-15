package pe.edu.utp.prisma_api.domain.folder;

import java.security.Principal;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.folder.dto.DeleteFolderDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderRequestDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderResponseDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.UpdateFolderDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class FolderWsController {

  private final FolderService folderService;
  private final RedisPublisher redisPublisher;

  @MessageMapping("/folder.create")
  public void createFolder(
      @Valid @Payload FolderRequestDTO dto,
      Principal principal) {

    UUID projectId = dto.getProjectId();

    UUID creatorId = UUID.fromString(principal.getName());
    FolderResponseDTO folder = folderService.create(projectId, creatorId, dto);

    String topic = "/topic/" + dto.getTeamId() + "/project/" + projectId + "/folders";
    System.out.println("Publicando en: " + topic);
    redisPublisher.publish(topic, folder);
  }

  @MessageMapping("/folder.delete")
  public void deleteFolder(
      @Payload DeleteFolderDTO dto,
      Principal principal) {

    folderService.delete(dto.getFolderId());

    redisPublisher.publish("/topic/" + dto.getTeamId() + "/project/" + dto.getProjectId() + "/folders/delete", dto);
  }

  @MessageMapping("/folder.update")
  public void updateFolder(@Payload UpdateFolderDTO dto, Principal principal) {
    FolderResponseDTO folder = folderService.update(dto.getFolderId(), dto);
    redisPublisher.publish("/topic/" + dto.getTeamId() + "/project/" + dto.getProjectId() + "/folders", folder);
  }
}
