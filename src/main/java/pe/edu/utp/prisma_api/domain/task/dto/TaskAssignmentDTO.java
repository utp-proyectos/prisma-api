package pe.edu.utp.prisma_api.domain.task.dto;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
public class TaskAssignmentDTO {
    private UUID id;
    private UUID userId;
    private String name;
    private String lastName;
    private boolean isDone;
}
