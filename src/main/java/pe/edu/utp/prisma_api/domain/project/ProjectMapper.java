package pe.edu.utp.prisma_api.domain.project;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pe.edu.utp.prisma_api.domain.calendar.model.CalendarEvent;
import pe.edu.utp.prisma_api.domain.kanban.KanbanMapper;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDashboardResponse;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDashboardResponse.DashboardEventDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDashboardResponse.DashboardTaskDTO;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDashboardResponse.TaskStatsDTO;
import pe.edu.utp.prisma_api.domain.task.Task;

@Mapper(componentModel = "spring", uses = KanbanMapper.class)
public interface ProjectMapper {
    ProjectResponse toDto(Project project);

    List<ProjectResponse> toDtoList(List<Project> projects);

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "description", source = "project.description")
    @Mapping(target = "coverImageUrl", source = "project.coverImageUrl")
    @Mapping(target = "taskStats", source = "stats")
    @Mapping(target = "upcomingTasks", source = "tasks")
    @Mapping(target = "todaysEvents", source = "events")
    ProjectDashboardResponse toDashboardResponse(
            Project project,
            TaskStatsDTO stats,
            List<DashboardTaskDTO> tasks,
            List<DashboardEventDTO> events);

    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "taskTitle", source = "task.title")
    @Mapping(target = "kanbanName", source = "task.column.kanban.name")
    @Mapping(target = "deadline", source = "task.deadline")
    @Mapping(target = "status", constant = "Pendiente")
    DashboardTaskDTO toDashboardTaskDto(Task task);

    List<DashboardTaskDTO> toDashboardTaskDtoList(List<Task> tasks);

    @Mapping(target = "eventId", source = "event.id")
    DashboardEventDTO toDashboardEventDto(CalendarEvent event);

    List<DashboardEventDTO> toDashboardEventDtoList(List<CalendarEvent> events);
}