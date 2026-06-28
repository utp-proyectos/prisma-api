package pe.edu.utp.prisma_api.domain.calendar;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class CalendarEventServiceImpl implements CalendarEventService {

    private final CalendarEventRepository repository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    public List<CalendarEvent> findAll() {
        return repository.findAll();
    }

    public Optional<CalendarEvent> findById(UUID id) {
        return repository.findById(id);
    }

    public CalendarEvent save(CalendarEvent entity) {
        return repository.save(entity);
    }

    public Optional<CalendarEvent> updateById(UUID id, CalendarEvent entity) {
        return repository.findById(id)
                .map(existing -> {
                    entity.setId(id);
                    return repository.save(entity);
                });
    }

    public void deleteById(UUID id) {
        CalendarEvent event = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendar event with id " + id + " not found"));

        event.setActive(false);
        repository.save(event);
    }

    @Override
    public CalendarEvent saveEvent(UUID projectId, UUID userId, CalendarEventRequest request) {
        validateRequest(request);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project with id " + projectId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found"));

        CalendarEvent event = new CalendarEvent();
        event.setTitle(request.getTitle().trim());
        event.setStartDate(request.getStartDate());
        event.setEndDate(getValidEndDate(request));
        event.setAllDay(request.getAllDay());
        event.setNotes(request.getNotes());
        event.setActive(true);
        event.setProject(project);
        event.setCreatedBy(user);

        if (Boolean.TRUE.equals(request.getAllDay())) {
            event.setStartTime(null);
            event.setEndTime(null);
        } else {
            event.setStartTime(request.getStartTime());
            event.setEndTime(request.getEndTime());
        }

        return repository.save(event);
    }

    @Override
    public Optional<CalendarEvent> updateEvent(UUID projectId, UUID eventId, CalendarEventRequest request) {
        validateRequest(request);

        return repository.findByIdAndProject_IdAndActiveTrue(eventId, projectId)
                .map(event -> {
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

                    return repository.save(event);
                });
    }

    @Override
    public List<CalendarEvent> findEventsByProject(UUID projectId) {
        return repository.findByProject_IdAndActiveTrueOrderByStartDateAsc(projectId);
    }

    @Override
    public List<CalendarEvent> findEventsByProjectAndDateRange(
            UUID projectId,
            LocalDate startDate,
            LocalDate endDate) {
        return repository.findByProjectAndDateRange(projectId, startDate, endDate);
    }

    @Override
    public List<CalendarItemDTO> getCalendarItems(
            UUID projectId,
            LocalDate startDate,
            LocalDate endDate) {
        List<CalendarItemDTO> calendarItems = new ArrayList<>();

        List<CalendarEvent> events = repository.findByProjectAndDateRange(projectId, startDate, endDate);
        for (CalendarEvent event : events) {
            calendarItems.add(convertEventToCalendarItem(event));
        }

        return calendarItems;
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

    private CalendarItemDTO convertEventToCalendarItem(CalendarEvent event) {
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