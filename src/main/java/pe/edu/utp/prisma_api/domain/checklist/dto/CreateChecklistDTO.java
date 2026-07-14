package pe.edu.utp.prisma_api.domain.checklist.dto;

import java.util.UUID;

import lombok.Data;
import pe.edu.utp.prisma_api.common.enums.Priority;

@Data
public class CreateChecklistDTO {
    private String title;
    private Priority priority;
    private UUID taskId;
}
