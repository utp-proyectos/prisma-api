package pe.edu.utp.prisma_api.domain.project.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ProjectDashboardResponse(
        UUID projectId,
        String projectName,
        String description,
        String coverImageUrl,
        TaskStatsDTO taskStats,
        List<DashboardTaskDTO> upcomingTasks,
        List<DashboardEventDTO> todaysEvents) {

    public static record TaskStatsDTO(
            long totalTasks,
            long completedTasks,
            long remainingTasks,
            double completedPercentage,
            double remainingPercentage) {
    }

    public static record DashboardTaskDTO(
            UUID taskId,
            String taskTitle,
            String kanbanName,
            LocalDate deadline,
            String status) {
    }

    public static record DashboardEventDTO(
            UUID eventId,
            String title,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime) {
    }
}