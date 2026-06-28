package pe.edu.utp.prisma_api.domain.project;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectRequest;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;

@RestController
@RequestMapping("/api/teams/{teamId}/projects")
@RequiredArgsConstructor
public class ProjectController {
  private final ProjectService projectService;

  @PostMapping
  public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
      @PathVariable UUID teamId,
      @Valid @RequestBody CreateProjectRequest request,
      @AuthenticationPrincipal UUID userId) {

    ProjectResponse project = projectService.createProject(
        teamId, request, userId);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.ok("Proyecto creado correctamente", project));
  }
}
