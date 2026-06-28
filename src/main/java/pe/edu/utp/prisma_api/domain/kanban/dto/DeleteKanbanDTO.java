package pe.edu.utp.prisma_api.domain.kanban.dto;

import lombok.Data;

@Data
public class DeleteKanbanDTO {
    private String projectId;
    private String kanbanId;
}
