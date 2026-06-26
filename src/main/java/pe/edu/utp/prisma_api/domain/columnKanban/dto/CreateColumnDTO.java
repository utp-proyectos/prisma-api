package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import lombok.Data;
import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;

@Data
public class CreateColumnDTO {
    private String title;
    private ColumnType type;
    private Integer position;
    private boolean isFixed;
}
