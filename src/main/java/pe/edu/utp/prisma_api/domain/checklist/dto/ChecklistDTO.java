package pe.edu.utp.prisma_api.domain.checklist.dto;

import java.util.List;

import lombok.Data;
import pe.edu.utp.prisma_api.common.enums.Priority;

@Data
public class ChecklistDTO {
    private String id;

    private String title;

    private Priority priority;

    private List<ChecklistItemDTO> items;
}
