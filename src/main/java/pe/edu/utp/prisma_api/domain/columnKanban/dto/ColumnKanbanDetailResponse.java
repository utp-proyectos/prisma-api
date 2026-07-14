package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDetailResponse;

@Getter
@Setter
public class ColumnKanbanDetailResponse {
    private UUID id;
    private String title;
    private Integer position;
    private boolean fixed;
    private ColumnType type;
    private List<TaskDetailResponse> tasks;

    private UUID kanbanId;
    private UUID projectId;
    private UUID teamId;
}
