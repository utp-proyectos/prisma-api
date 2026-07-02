package pe.edu.utp.prisma_api.domain.task.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.*;
import pe.edu.utp.prisma_api.common.enums.Priority;

@Getter
@Setter
public class UpdateTaskDTO {
    private UUID id;

    private String title;

    private String description;

    private LocalDate deadline;

    private Priority priority;

    private Boolean groupTask;

    private Boolean completed;

    private UUID columnId;

    private UUID milestoneId;

    private UUID kanbanId;

    private UUID projectId;

    private UUID teamId;

    private List<UUID> assignedUserIds;
}
