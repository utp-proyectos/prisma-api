package pe.edu.utp.prisma_api.domain.calendar;

import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarEventRequest;
import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarItemDTO;
import pe.edu.utp.prisma_api.domain.calendar.model.CalendarEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalendarEventService {

        CalendarEvent saveEvent(UUID projectId, UUID userId, CalendarEventRequest request);

        Optional<CalendarEvent> updateEvent(UUID projectId, UUID eventId, CalendarEventRequest request);

        List<CalendarEvent> findEventsByProject(UUID projectId);

        List<CalendarEvent> findEventsByProjectAndDateRange(
                        UUID projectId,
                        LocalDate startDate,
                        LocalDate endDate);

        List<CalendarItemDTO> getCalendarItems(
                        UUID projectId,
                        LocalDate startDate,
                        LocalDate endDate);
}