package pe.edu.utp.prisma_api.domain.team;

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
import pe.edu.utp.prisma_api.domain.team.dto.CreateTeamRequest;
import pe.edu.utp.prisma_api.domain.team.dto.TeamDetailResponse;
import pe.edu.utp.prisma_api.domain.team.dto.TeamSummaryResponse;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

  private final TeamService teamService;

  @PostMapping
  public ResponseEntity<ApiResponse<TeamSummaryResponse>> createTeam(@Valid @RequestBody CreateTeamRequest request,
      @AuthenticationPrincipal UUID userId) {

    TeamSummaryResponse team = teamService.createTeam(request, userId);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.ok("Equipo creado correctamente", team));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<TeamSummaryResponse>>> getMyTeams(@AuthenticationPrincipal UUID userId) {

    return ResponseEntity.ok(
        ApiResponse.ok(teamService.getMyTeams(userId)));
  }

  @GetMapping("/{teamId}")
  public ResponseEntity<ApiResponse<TeamDetailResponse>> getTeam(@PathVariable UUID teamId,
      @AuthenticationPrincipal UUID userId) {

    return ResponseEntity.ok(
        ApiResponse.ok(teamService.getTeamById(teamId, userId)));
  }
}
