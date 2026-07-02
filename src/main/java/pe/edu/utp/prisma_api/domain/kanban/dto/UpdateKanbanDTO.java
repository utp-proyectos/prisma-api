package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
public class UpdateKanbanDTO {
    private UUID kanbanId;
    private String name;
    private boolean isPrivate;
}
