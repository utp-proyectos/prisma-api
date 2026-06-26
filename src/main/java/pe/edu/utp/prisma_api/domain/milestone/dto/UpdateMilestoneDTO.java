package pe.edu.utp.prisma_api.domain.milestone.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateMilestoneDTO {
    private String title;
    private LocalDate dueDate;
}
