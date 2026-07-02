package pe.edu.utp.prisma_api.domain.kanban.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateKanbanDTO {
    private String name;
    private Boolean privateSwitch;
    private UUID projectId;
}