package pe.edu.utp.prisma_api.domain.kanban.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
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

}
