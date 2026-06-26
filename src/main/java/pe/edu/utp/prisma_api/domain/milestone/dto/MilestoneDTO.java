package pe.edu.utp.prisma_api.domain.milestone.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDTO;

@Data
public class MilestoneDTO {
    private String id;

    private String title;

    private LocalDate dueDate;

    private List<TaskDTO> tasks;
}
