package pe.edu.utp.prisma_api.domain.project.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskSummaryDTO {
    private String kanbanName;
    private String taskTitle;
    private LocalDate dueDate;
    private String priority;
}