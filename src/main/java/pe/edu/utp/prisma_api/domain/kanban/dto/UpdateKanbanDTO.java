package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UpdateKanbanDTO {
    private String name;
    private boolean isPrivate;
    private UUID kanbanId;
    private UUID projectId;
}
