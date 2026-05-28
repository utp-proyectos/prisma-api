package pe.edu.utp.prisma_api.domain.project.dto;

import lombok.Data;

@Data
public class TaskSummaryDTO {
    private String kanbanName;
    private String taskTitle;
    private java.time.Instant dueDate;
    private String priority;
}