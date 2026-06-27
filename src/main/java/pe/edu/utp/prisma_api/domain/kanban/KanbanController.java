package pe.edu.utp.prisma_api.domain.kanban;

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
import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.services.KanbanService;

@RestController
@RequestMapping("/api/kanbans")
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<KanbanDTO>>> findAllByProject(
            @PathVariable String projectId) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        kanbanService.findAllByProjectId(projectId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KanbanDTO>> findById(
            @PathVariable String id) {

        KanbanDTO kanban = kanbanService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        return ResponseEntity.ok(
                ApiResponse.ok(kanban));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KanbanDTO>> create(
            @RequestParam String projectId,
            @RequestParam String creatorId,
            @Valid @RequestBody CreateKanbanDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Kanban creado correctamente",
                        kanbanService.save(projectId, creatorId, dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KanbanDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateKanbanDTO dto) {

        KanbanDTO kanban = kanbanService.update(id, dto)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Kanban actualizado correctamente",
                        kanban));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable String id) {

        kanbanService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "Kanban eliminado correctamente",
                        null));
    }

}
