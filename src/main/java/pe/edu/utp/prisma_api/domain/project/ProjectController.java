package pe.edu.utp.prisma_api.domain.project;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectHomeDTO;
import pe.edu.utp.prisma_api.domain.project.dto.UpdateProjectDTO;
import pe.edu.utp.prisma_api.domain.project.services.ProjectService;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> findById(
            @PathVariable String id) {

        ProjectDTO project = projectService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        return ResponseEntity.ok(
                ApiResponse.ok(project));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectDTO>> save(
            @RequestParam String teamId,
            @Valid @RequestBody CreateProjectDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Proyecto creado correctamente",
                        projectService.save(teamId, dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateProjectDTO dto) {

        ProjectDTO project = projectService.update(id, dto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Proyecto actualizado correctamente",
                        project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable String id) {

        projectService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Proyecto eliminado correctamente",
                        null));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<ProjectDTO>>> findRecentByUser(
            @RequestParam String userId) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        projectService.findRecentByUser(userId)));
    }

    @GetMapping("/{id}/home")
    public ResponseEntity<ApiResponse<ProjectHomeDTO>> getHomeSummary(
            @PathVariable String id) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        projectService.getHomeSummary(id)));
    }

}