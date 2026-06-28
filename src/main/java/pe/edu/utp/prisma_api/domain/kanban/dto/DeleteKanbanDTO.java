package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class DeleteKanbanDTO {
    private UUID projectId;
    private UUID kanbanId;
}
