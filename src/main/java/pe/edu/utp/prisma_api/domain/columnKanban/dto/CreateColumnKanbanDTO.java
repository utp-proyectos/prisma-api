package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
public class CreateColumnKanbanDTO {
    private String title;
    private UUID kanbanId;
    private UUID projectId;
    private UUID teamId;
}
