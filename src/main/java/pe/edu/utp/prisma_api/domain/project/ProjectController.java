package pe.edu.utp.prisma_api.domain.project;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectRequest;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDashboardResponse;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;

@RestController
@RequestMapping("/api/teams/{teamId}/projects")
@RequiredArgsConstructor
public class ProjectController {
  private final ProjectService projectService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProjectResponse>>> findAllByTeam(
      @PathVariable UUID teamId,
      @AuthenticationPrincipal UUID userId) {

    List<ProjectResponse> proyectos = projectService.findAllByTeamId(teamId, userId);

    return ResponseEntity.ok(ApiResponse.ok("Proyectos del equipo obtenidos con éxito", proyectos));
  }

  @GetMapping("/{projectId}/dashboard")
  public ResponseEntity<ApiResponse<ProjectDashboardResponse>> getProjectDashboard(
      @PathVariable UUID projectId,
      @AuthenticationPrincipal UUID userId) {

    ProjectDashboardResponse dashboardData = projectService.getProjectDashboard(projectId, userId);

    return ResponseEntity.ok(ApiResponse.ok("Dashboard obtenido con éxito", dashboardData));
  }

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
