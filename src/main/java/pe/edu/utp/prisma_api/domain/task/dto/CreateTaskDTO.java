package pe.edu.utp.prisma_api.domain.task.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;
import pe.edu.utp.prisma_api.common.enums.Priority;

@Getter
@Setter
public class CreateTaskDTO {
    private String title;

    private String description;

    private LocalDate deadline;

    private UUID columnId;

    private UUID milestoneId;

    private Priority priority;

    private Boolean groupTask;

    private UUID kanbanId;

    private UUID projectId;

    private UUID teamId;
}