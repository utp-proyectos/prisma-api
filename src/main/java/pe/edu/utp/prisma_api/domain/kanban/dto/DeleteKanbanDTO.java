package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
public class DeleteKanbanDTO {
    private UUID kanbanId;
}
