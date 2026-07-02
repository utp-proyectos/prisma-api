package pe.edu.utp.prisma_api.domain.columnKanban.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ColumnOrderDTO {
    private UUID id;
    private Integer position;
}