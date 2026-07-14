package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
public class KanbanDTO {
    private UUID id;
    private UUID projectId;
    private UUID teamId;
    private String name;
    private boolean privateSwitch;
    private UUID creatorId;
}