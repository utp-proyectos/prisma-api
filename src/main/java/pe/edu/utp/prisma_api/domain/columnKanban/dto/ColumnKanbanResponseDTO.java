package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import lombok.Data;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;

@Data
public class ColumnKanbanResponseDTO {
    private String id;
    private String name;
    private ColumnType type; // Tu Enum (BACKLOG, IN_PROGRESS, COMPLETED)
    // private List<TaskSummaryDTO> tasks; // Reutilizamos el DTO de tareas que ya
    // tienes
}
