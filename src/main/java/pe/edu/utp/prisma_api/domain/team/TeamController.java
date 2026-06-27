package pe.edu.utp.prisma_api.domain.team;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.team.dto.CreateTeamDTO;
import pe.edu.utp.prisma_api.domain.team.dto.TeamDTO;
import pe.edu.utp.prisma_api.domain.team.dto.UpdateTeamDTO;
import pe.edu.utp.prisma_api.domain.team.services.TeamService;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

        private final TeamService teamService;

        @GetMapping
        public ResponseEntity<ApiResponse<List<TeamDTO>>> findAll() {

                return ResponseEntity.ok(
                                ApiResponse.ok(teamService.findAll()));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<TeamDTO>> findById(
                        @PathVariable String id) {

                TeamDTO team = teamService.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));

                return ResponseEntity.ok(
                                ApiResponse.ok(team));
        }

        @PostMapping
        public ResponseEntity<ApiResponse<TeamDTO>> save(
                        @RequestParam String ownerId,
                        @Valid @RequestBody CreateTeamDTO dto) {

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.ok(
                                                "Equipo creado correctamente",
                                                teamService.save(dto, ownerId)));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<TeamDTO>> update(
                        @PathVariable String id,
                        @Valid @RequestBody UpdateTeamDTO dto) {

                TeamDTO team = teamService.update(id, dto)
                                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                "Equipo actualizado correctamente",
                                                team));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(
                        @PathVariable String id) {

                teamService.delete(id);

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                "Equipo eliminado correctamente",
                                                null));
        }

        @GetMapping("/user/{userId}")
        public ResponseEntity<ApiResponse<List<TeamDTO>>> findByUser(
                        @PathVariable String userId) {

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                teamService.findByUser(userId)));
        }

        @PatchMapping("/{teamId}/members/{userId}/role")
        public ResponseEntity<ApiResponse<Void>> updateMemberRole(
                        @PathVariable String teamId,
                        @PathVariable String userId,
                        @RequestParam String adminId,
                        @RequestParam TeamRole role) {

                teamService.updateMemberRole(
                                teamId,
                                adminId,
                                userId,
                                role);

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                "Rol actualizado correctamente",
                                                null));
        }
}