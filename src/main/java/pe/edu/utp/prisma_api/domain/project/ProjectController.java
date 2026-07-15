package pe.edu.utp.prisma_api.domain.project;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
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

    List<ProjectResponse> projects = projectService.findAllByTeamId(teamId, userId);

    return ResponseEntity.ok(ApiResponse.ok("Proyectos del equipo obtenidos con éxito", projects));
  }

  @GetMapping("/{projectId}/dashboard")
  public ResponseEntity<ApiResponse<ProjectDashboardResponse>> getProjectDashboard(
      @PathVariable UUID projectId,
      @AuthenticationPrincipal UUID userId) {

    ProjectDashboardResponse dashboardData = projectService.getProjectDashboard(projectId, userId);

    return ResponseEntity.ok(ApiResponse.ok("Dashboard obtenido con éxito", dashboardData));
  }
}
