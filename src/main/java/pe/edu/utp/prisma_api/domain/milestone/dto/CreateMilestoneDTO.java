package pe.edu.utp.prisma_api.domain.milestone.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMilestoneDTO {
    private String title;
    private LocalDate deadline;
    private UUID kanbanId;
}
