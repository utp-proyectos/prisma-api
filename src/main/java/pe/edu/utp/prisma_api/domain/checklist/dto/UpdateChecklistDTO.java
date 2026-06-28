package pe.edu.utp.prisma_api.domain.checklist.dto;

import lombok.Data;
import pe.edu.utp.prisma_api.common.enums.Priority;

@Data
public class UpdateChecklistDTO {
    private String title;

    private Priority priority;
}
