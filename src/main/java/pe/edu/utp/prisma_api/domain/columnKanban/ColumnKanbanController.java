package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.List;
import java.util.UUID;

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
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.UpdateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.services.ColumnKanbanService;

@RestController
@RequestMapping("/api/columns")
@RequiredArgsConstructor
public class ColumnKanbanController {

        private final ColumnKanbanService columnService;

        @GetMapping("/kanban/{kanbanId}")
        public ResponseEntity<ApiResponse<List<ColumnKanbanDTO>>> findAllByKanban(
                        @PathVariable UUID kanbanId) {

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                columnService.findAllByKanban(kanbanId)));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ColumnKanbanDTO>> findById(
                        @PathVariable UUID id) {

                ColumnKanbanDTO column = columnService.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Columna no encontrada"));

                return ResponseEntity.ok(
                                ApiResponse.ok(column));
        }

        @PostMapping
        public ResponseEntity<ApiResponse<ColumnKanbanDTO>> create(
                        @RequestParam UUID kanbanId,
                        @Valid @RequestBody CreateColumnKanbanDTO dto) {

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.ok(
                                                                "Columna creada correctamente",
                                                                columnService.save(kanbanId, dto)));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<ColumnKanbanDTO>> update(
                        @PathVariable UUID id,
                        @Valid @RequestBody UpdateColumnKanbanDTO dto) {

                ColumnKanbanDTO column = columnService.update(id, dto)
                                .orElseThrow(() -> new ResourceNotFoundException("Columna no encontrada"));

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                "Columna actualizada correctamente",
                                                column));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(
                        @PathVariable UUID id) {

                columnService.delete(id);

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                "Columna eliminada correctamente",
                                                null));
        }

        @PutMapping("/kanban/{kanbanId}/reorder")
        public ResponseEntity<ApiResponse<Void>> reorder(
                        @PathVariable UUID kanbanId,
                        @RequestBody List<UUID> columnIds) {

                columnService.reorder(kanbanId, columnIds);

                return ResponseEntity.ok(
                                ApiResponse.ok(
                                                "Columnas reordenadas correctamente",
                                                null));
        }

}