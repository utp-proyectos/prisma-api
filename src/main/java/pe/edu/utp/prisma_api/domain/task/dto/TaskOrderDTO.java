package pe.edu.utp.prisma_api.domain.task.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class TaskOrderDTO {
    private UUID id;
    private Integer position;
}