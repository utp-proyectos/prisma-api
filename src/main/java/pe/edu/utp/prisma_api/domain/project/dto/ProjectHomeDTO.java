package pe.edu.utp.prisma_api.domain.project.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectHomeDTO {
    private String id;
    private String name;
    private String description;

    // Datos para el gráfico de dona (Estado de tareas)
    private long totalTasks;
    private long completedTasks;
    private long remainingTasks;
    private double completionPercentage;

    // Listas filtradas para los widgets
    // private List<TaskSummaryDTO> upcomingTasks; // Solo las más cercanas
    // private List<EventDTO> todayEvents; // Solo eventos de hoy
}
