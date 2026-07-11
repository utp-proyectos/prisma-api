package pe.edu.utp.prisma_api.domain.task.dto;

import java.util.Set;
import java.util.UUID;

public record TaskOperationResult(
        TaskDetailResponse task,
        Set<UUID> affectedMilestoneIds) {
}