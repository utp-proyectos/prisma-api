package pe.edu.utp.prisma_api.domain.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.exception.UnauthorizedException;
import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarEventRequest;
import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarItemDTO;
import pe.edu.utp.prisma_api.domain.calendar.enums.CalendarItemType;
import pe.edu.utp.prisma_api.domain.calendar.model.CalendarEvent;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;
import pe.edu.utp.prisma_api.domain.task.Task;
import pe.edu.utp.prisma_api.domain.task.TaskRepository;

@Service
@RequiredArgsConstructor
public class CalendarEventServiceImpl implements CalendarEventService {

    private final CalendarEventRepository repository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public CalendarEvent saveEvent(UUID projectId, UUID userId, CalendarEventRequest request) {
        validateRequest(request);

        if (userId == null) {
            throw new UnauthorizedException("Usuario autenticado requerido para crear evento");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        CalendarEvent event = new CalendarEvent();
        applyRequestToEvent(event, request);

        event.setActive(true);
        event.setProject(project);
        event.setCreatedBy(user);

        return repository.save(event);
    }

    @Override
    public Optional<CalendarEvent> updateEvent(UUID projectId, UUID eventId, CalendarEventRequest request) {
        validateRequest(request);

        return repository.findByIdAndProject_IdAndActiveTrue(eventId, projectId)
                .map(event -> {
                    applyRequestToEvent(event, request);
                    return repository.save(event);
                });
    }

    @Override
    public void deleteEvent(UUID projectId, UUID eventId) {
        CalendarEvent event = repository.findByIdAndProject_IdAndActiveTrue(eventId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento de calendario no encontrado"));

        event.setActive(false);
        repository.save(event);
    }

    @Override
    public List<CalendarEvent> findEventsByProject(UUID projectId) {
        return repository.findByProject_IdAndActiveTrueOrderByStartDateAsc(projectId);
    }

    @Override
    public List<CalendarEvent> findEventsByProjectAndDateRange(
            UUID projectId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return repository.findByProjectAndDateRange(projectId, startDate, endDate);
    }

    @Override
    public List<CalendarItemDTO> getCalendarItems(
            UUID projectId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<CalendarItemDTO> calendarItems = new ArrayList<>();

        List<CalendarEvent> events = repository.findByProjectAndDateRange(
                projectId,
                startDate,
                endDate
        );

        for (CalendarEvent event : events) {
            calendarItems.add(convertEventToCalendarItem(event));
        }

        List<Task> deadlines = taskRepository.findDeadlinesByProjectAndDateRange(
                projectId,
                startDate,
                endDate
        );

        for (Task task : deadlines) {
            calendarItems.add(convertTaskToCalendarItem(task));
        }

        return calendarItems;
    }

    @Override
    public CalendarItemDTO convertEventToCalendarItem(CalendarEvent event) {
        CalendarItemDTO item = new CalendarItemDTO();

        item.setId("event-" + event.getId());
        item.setTitle(event.getTitle());
        item.setStart(buildEventStart(event));
        item.setEnd(buildEventEnd(event));
        item.setAllDay(event.getAllDay());
        item.setType(CalendarItemType.EVENT);
        item.setSourceId(String.valueOf(event.getId()));
        item.setEditable(true);
        item.setColor("#3788d8");
        item.setNotes(event.getNotes());

        return item;
    }

    private CalendarItemDTO convertTaskToCalendarItem(Task task) {
        CalendarItemDTO item = new CalendarItemDTO();

        item.setId("deadline-" + task.getId());
        item.setTitle(task.getTitle());
        item.setStart(task.getDeadline().toString());
        item.setEnd(task.getDeadline().plusDays(1).toString());
        item.setAllDay(true);
        item.setType(CalendarItemType.DEADLINE);
        item.setSourceId(String.valueOf(task.getId()));
        item.setEditable(false);
        item.setColor("#f59e0b");
        item.setNotes(task.getDescription());

        return item;
    }

    private void applyRequestToEvent(CalendarEvent event, CalendarEventRequest request) {
        event.setTitle(request.getTitle().trim());
        event.setStartDate(request.getStartDate());
        event.setEndDate(getValidEndDate(request));
        event.setAllDay(request.getAllDay());
        event.setNotes(request.getNotes());

        if (Boolean.TRUE.equals(request.getAllDay())) {
            event.setStartTime(null);
            event.setEndTime(null);
        } else {
            event.setStartTime(request.getStartTime());
            event.setEndTime(request.getEndTime());
        }
    }

    private void validateRequest(CalendarEventRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Event title is required");
        }

        if (request.getStartDate() == null) {
            throw new RuntimeException("Start date is required");
        }

        if (request.getAllDay() == null) {
            throw new RuntimeException("All day field is required");
        }

        LocalDate endDate = getValidEndDate(request);

        if (endDate.isBefore(request.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        if (Boolean.FALSE.equals(request.getAllDay())) {
            if (request.getStartTime() == null) {
                throw new RuntimeException("Start time is required when event is not all day");
            }

            if (request.getEndTime() == null) {
                throw new RuntimeException("End time is required when event is not all day");
            }

            if (request.getStartDate().equals(endDate)
                    && !request.getEndTime().isAfter(request.getStartTime())) {
                throw new RuntimeException("End time must be after start time");
            }
        }
    }

    private LocalDate getValidEndDate(CalendarEventRequest request) {
        return request.getEndDate() == null ? request.getStartDate() : request.getEndDate();
    }

    private String buildEventStart(CalendarEvent event) {
        if (Boolean.TRUE.equals(event.getAllDay())) {
            return event.getStartDate().toString();
        }

        return event.getStartDate() + "T" + event.getStartTime();
    }

    private String buildEventEnd(CalendarEvent event) {
        if (Boolean.TRUE.equals(event.getAllDay())) {
            return event.getEndDate().plusDays(1).toString();
        }

        return event.getEndDate() + "T" + event.getEndTime();
    }
}