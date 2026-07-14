package pe.edu.utp.prisma_api.domain.milestone.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import pe.edu.utp.prisma_api.domain.milestone.enums.StateMilestone;

@Getter
@Setter
public class MilestoneSummaryResponse {
    private UUID id;

    private String title;

    private LocalDate deadline;

    private Integer progress;

    private Integer totalTasks;

    private Integer completedTasks;

    private StateMilestone state;

    private UUID kanbanId;
    private UUID projectId;
    private UUID teamId;
}
