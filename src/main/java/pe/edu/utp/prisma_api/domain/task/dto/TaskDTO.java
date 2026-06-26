package pe.edu.utp.prisma_api.domain.task.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import pe.edu.utp.prisma_api.common.enums.Priority;
import pe.edu.utp.prisma_api.domain.checklist.dto.ChecklistDTO;

@Data
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private Integer position;
    private LocalDate dueDate;
    private boolean isGroupTask;
    private Priority priority;
    private String milestoneId;
    private List<TaskAssignmentDTO> assignments;
    private List<ChecklistDTO> checklists;
}
