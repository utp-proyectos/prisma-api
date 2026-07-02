package pe.edu.utp.prisma_api.domain.task.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
public class ReorderTasksDTO {
    private UUID taskId;
    private UUID targetColumnId;
    private List<TaskOrderDTO> targetTasks;
    private UUID teamId;
    private UUID projectId;
    private UUID kanbanId;
}