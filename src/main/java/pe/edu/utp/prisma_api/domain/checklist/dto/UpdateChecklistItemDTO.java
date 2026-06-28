package pe.edu.utp.prisma_api.domain.checklist.dto;

import lombok.Data;

@Data
public class UpdateChecklistItemDTO {
    private String content;

    private boolean isCompleted;
}
