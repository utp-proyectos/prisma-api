package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDetailResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KanbanMyTasksResponse {
    private UUID id;
    private String name;
    private List<TaskDetailResponse> tasks;
}