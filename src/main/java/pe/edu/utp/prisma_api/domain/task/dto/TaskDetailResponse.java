package pe.edu.utp.prisma_api.domain.task.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.*;
import pe.edu.utp.prisma_api.common.enums.Priority;
import pe.edu.utp.prisma_api.domain.checklist.dto.ChecklistDetailResponse;

@Getter
@Setter
public class TaskDetailResponse {

    private UUID id;

    private String title;

    private String description;

    private Integer position;

    private LocalDate deadline;

    private Priority priority;

    private boolean completed;

    private boolean groupTask;

    private UUID columnId;

    private UUID milestoneId;

    private List<TaskAssignmentDTO> assignments;

    private List<ChecklistDetailResponse> checklists;
}