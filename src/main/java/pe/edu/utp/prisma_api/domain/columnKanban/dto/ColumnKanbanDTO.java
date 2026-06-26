package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import java.util.List;

import lombok.Data;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDTO;

@Data
public class ColumnKanbanDTO {
    private String id;
    private String title;
    private Integer position;
    private boolean isFixed;
    private ColumnType type;
    private List<TaskDTO> tasks;
}
