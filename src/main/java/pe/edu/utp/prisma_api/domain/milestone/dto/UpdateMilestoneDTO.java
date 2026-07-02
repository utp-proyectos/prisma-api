package pe.edu.utp.prisma_api.domain.milestone.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMilestoneDTO {
    private String title;
    private LocalDate deadline;
}
