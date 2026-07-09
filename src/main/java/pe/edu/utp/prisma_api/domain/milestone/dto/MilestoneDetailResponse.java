package pe.edu.utp.prisma_api.domain.milestone.dto;

import java.time.LocalDate;
import java.util.UUID;

import pe.edu.utp.prisma_api.domain.milestone.enums.StateMilestone;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDetailResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MilestoneDetailResponse {
    private UUID id;

    private String title;

    private LocalDate deadline;

    private Integer progress;

    private Integer totalTasks;

    private Integer completedTasks;

    private StateMilestone state;

    private TaskDetailResponse[] tasks;

    private UUID kanbanId;
    private UUID projectId;
    private UUID teamId;
}
