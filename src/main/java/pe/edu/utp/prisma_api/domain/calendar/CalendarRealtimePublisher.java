package pe.edu.utp.prisma_api.domain.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarItemDTO;
import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarWsMessage;
import pe.edu.utp.prisma_api.domain.calendar.enums.CalendarWsAction;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalendarRealtimePublisher {

    private final RedisPublisher redisPublisher;
    private final ProjectRepository projectRepository;

    @Transactional(readOnly = true)
    public void publishCreated(UUID projectId, CalendarItemDTO item) {
        publish(projectId, CalendarWsAction.CREATED, item.getSourceId(), item);
    }

    @Transactional(readOnly = true)
    public void publishUpdated(UUID projectId, CalendarItemDTO item) {
        publish(projectId, CalendarWsAction.UPDATED, item.getSourceId(), item);
    }

    @Transactional(readOnly = true)
    public void publishDeleted(UUID projectId, UUID eventId) {
        publish(projectId, CalendarWsAction.DELETED, eventId.toString(), null);
    }

    private void publish(
            UUID projectId,
            CalendarWsAction action,
            String sourceId,
            CalendarItemDTO item
    ) {
        UUID teamId = resolveTeamId(projectId);

        CalendarWsMessage message = new CalendarWsMessage(
                action,
                projectId,
                sourceId,
                item
        );

        redisPublisher.publish(
                buildTopic(teamId, projectId),
                message
        );
    }

    private UUID resolveTeamId(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        return project.getTeam().getId();
    }

    private String buildTopic(UUID teamId, UUID projectId) {
        return "/topic/" + teamId + "/projects/" + projectId + "/calendar";
    }
}