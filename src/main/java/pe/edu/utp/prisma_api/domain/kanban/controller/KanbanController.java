package pe.edu.utp.prisma_api.domain.kanban.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.kanban.KanbanService;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDetailResponse;

@RestController
@RequiredArgsConstructor
public class KanbanController {

        private final KanbanService kanbanService;

        @GetMapping("/api/projects/{projectId}/kanbans")
        public ResponseEntity<ApiResponse<List<KanbanDTO>>> findAllByProject(
                        @PathVariable UUID projectId,
                        Principal principal) {

                UUID userId = UUID.fromString(principal.getName());

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                kanbanService.findAllByProjectId(projectId, userId)));
        }

        @GetMapping("/api/kanbans/{kanbanId}")
        public ResponseEntity<ApiResponse<KanbanDetailResponse>> findById(
                        @PathVariable UUID kanbanId, Principal principal) {

                UUID userId = UUID.fromString(principal.getName());

                KanbanDetailResponse kanban = kanbanService.findById(kanbanId, userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

                return ResponseEntity.ok(
                                ApiResponse.ok(kanban));
        }

}
