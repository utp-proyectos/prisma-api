package pe.edu.utp.prisma_api.domain.task.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTaskDTO {
    private UUID id;
    private UUID kanbanId;
    private UUID projectId;
    private UUID teamId;
}
