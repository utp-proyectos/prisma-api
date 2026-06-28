package pe.edu.utp.prisma_api.domain.task.dto;

import lombok.Data;

@Data
public class TaskAssignmentDTO {
    private String id;

    private String userId;

    private boolean isDone;
}
