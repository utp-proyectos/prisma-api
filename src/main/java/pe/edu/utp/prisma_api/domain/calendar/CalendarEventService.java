package pe.edu.utp.prisma_api.domain.calendar;

import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarEventRequest;
import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarItemDTO;
import pe.edu.utp.prisma_api.domain.calendar.model.CalendarEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarEventService {

    CalendarEvent saveEvent(String projectId, String userId, CalendarEventRequest request);

    Optional<CalendarEvent> updateEvent(String projectId, Integer eventId, CalendarEventRequest request);

    List<CalendarEvent> findEventsByProject(String projectId);

    List<CalendarEvent> findEventsByProjectAndDateRange(
            String projectId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<CalendarItemDTO> getCalendarItems(
            String projectId,
            LocalDate startDate,
            LocalDate endDate
    );
}