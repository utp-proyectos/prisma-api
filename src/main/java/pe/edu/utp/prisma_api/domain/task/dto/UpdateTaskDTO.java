package pe.edu.utp.prisma_api.domain.task.dto;

import java.time.LocalDate;
import lombok.Data;
import pe.edu.utp.prisma_api.common.enums.Priority;

@Data
public class UpdateTaskDTO {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate deadline;
    private boolean isGroupTask;
    private Priority priority;
    private String milestoneId;
}
