package pe.edu.utp.prisma_api.domain.kanban.dto;

import lombok.Data;

@Data
public class UpdateKanbanDTO {
    private String name;
    private boolean isPrivate;
    private String kanbanId;
    private String projectId;
}
