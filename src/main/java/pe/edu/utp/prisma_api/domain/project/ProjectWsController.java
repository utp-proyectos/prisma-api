package pe.edu.utp.prisma_api.domain.project;

import java.security.Principal;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectRequest;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class ProjectWsController {
  private final ProjectService projectService;
  private final RedisPublisher redisPublisher;

  @MessageMapping("/project.create")
  public void createProject(
      @Valid @Payload CreateProjectRequest request,
      Principal principal) {
    ProjectResponse project = projectService.createProject(
        request.getTeamId(), request, UUID.fromString(principal.getName()));

    redisPublisher.publish("/topic/" + request.getTeamId() + "/projects", project);

  }
}
