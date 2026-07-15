package pe.edu.utp.prisma_api.domain.project;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.channel.Channel;
import pe.edu.utp.prisma_api.domain.channel.ChannelRepository;
import pe.edu.utp.prisma_api.domain.calendar.CalendarEventRepository;
import pe.edu.utp.prisma_api.domain.calendar.model.CalendarEvent;
import pe.edu.utp.prisma_api.domain.project.dto.CreateProjectRequest;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDashboardResponse;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectDashboardResponse.TaskStatsDTO;
import pe.edu.utp.prisma_api.domain.task.Task;
import pe.edu.utp.prisma_api.domain.task.TaskRepository;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;
import pe.edu.utp.prisma_api.domain.team.Team;
import pe.edu.utp.prisma_api.domain.team.TeamRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final TeamRepository teamRepository;
    private final ChannelRepository channelRepository;
    private final ProjectMapper projectMapper;

    public List<ProjectResponse> findAllByTeamId(UUID teamId, UUID userId) {
        List<Project> projects = projectRepository.findByTeamId(teamId);

        return projectMapper.toDtoList(projects);
    }

    public ProjectResponse createProject(CreateProjectRequest request) {
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setTeam(team);
        projectRepository.save(project);

        Channel channel = new Channel();
        channel.setName("General");
        channel.setProject(project);

        channelRepository.save(channel);

        return toDto(project);
    }

    private ProjectResponse toDto(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt());
    }

    public ProjectDashboardResponse getProjectDashboard(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        List<Task> myTasks = taskRepository.findTasksByProjectAndUser(projectId, userId);

        long totalTasks = myTasks.size();

        long completedTasks = myTasks.stream()
                .filter(t -> {
                    String colName = t.getColumn().getTitle().toLowerCase();
                    return colName.equals("done") || colName.contains("completado");
                })
                .count();

        long remainingTasks = totalTasks - completedTasks;

        double completedPercentage = totalTasks > 0 ? ((double) completedTasks / totalTasks) * 100 : 0.0;
        double remainingPercentage = totalTasks > 0 ? ((double) remainingTasks / totalTasks) * 100 : 0.0;

        TaskStatsDTO stats = new TaskStatsDTO(
                totalTasks,
                completedTasks,
                remainingTasks,
                Math.round(completedPercentage),
                Math.round(remainingPercentage));

        List<Task> upcomingTasks = myTasks.stream()
                .filter(t -> {
                    String colName = t.getColumn().getTitle().toLowerCase();
                    return !colName.equals("done") && !colName.contains("completado");
                })
                .sorted(Comparator.comparing(
                        (Task task) -> task != null ? task.getDeadline() : null,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(5)
                .toList();

        LocalDate today = LocalDate.now();
        List<CalendarEvent> todaysEvents = calendarEventRepository.findActiveEventsForToday(projectId, today);

        return projectMapper.toDashboardResponse(
                project,
                stats,
                projectMapper.toDashboardTaskDtoList(upcomingTasks),
                projectMapper.toDashboardEventDtoList(todaysEvents));
    }
}