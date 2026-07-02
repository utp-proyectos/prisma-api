package pe.edu.utp.prisma_api.domain.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarEventRequest;
import pe.edu.utp.prisma_api.domain.calendar.dto.CalendarItemDTO;
import pe.edu.utp.prisma_api.domain.calendar.model.CalendarEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/calendar")
@RequiredArgsConstructor
public class CalendarController {

        private final CalendarEventService calendarEventService;
        private final CalendarRealtimePublisher calendarRealtimePublisher;

        @GetMapping
        public ResponseEntity<ApiResponse<List<CalendarItemDTO>>> getCalendarItems(
                        @PathVariable UUID projectId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
                List<CalendarItemDTO> items = calendarEventService.getCalendarItems(
                                projectId,
                                startDate,
                                endDate);

                return ResponseEntity.ok(
                                ApiResponse.ok(items));
        }

        @PostMapping("/events")
        public ResponseEntity<ApiResponse<CalendarItemDTO>> createEvent(
                        @PathVariable UUID projectId,
                        @RequestBody CalendarEventRequest request,
                        @AuthenticationPrincipal UUID userId) {
                CalendarEvent event = calendarEventService.saveEvent(
                                projectId,
                                userId,
                                request);

                CalendarItemDTO item = calendarEventService.convertEventToCalendarItem(event);

                calendarRealtimePublisher.publishCreated(projectId, item);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponse.ok("Evento creado correctamente", item));
        }

        @PutMapping("/events/{eventId}")
        public ResponseEntity<ApiResponse<CalendarItemDTO>> updateEvent(
                        @PathVariable UUID projectId,
                        @PathVariable UUID eventId,
                        @RequestBody CalendarEventRequest request) {
                CalendarEvent event = calendarEventService.updateEvent(
                                projectId,
                                eventId,
                                request)
                                .orElseThrow(() -> new ResourceNotFoundException("Evento de calendario no encontrado"));

                CalendarItemDTO item = calendarEventService.convertEventToCalendarItem(event);

                calendarRealtimePublisher.publishUpdated(projectId, item);

                return ResponseEntity.ok(
                                ApiResponse.ok("Evento actualizado correctamente", item));
        }

        @DeleteMapping("/events/{eventId}")
        public ResponseEntity<ApiResponse<Void>> deleteEvent(
                        @PathVariable UUID projectId,
                        @PathVariable UUID eventId) {
                calendarEventService.deleteEvent(projectId, eventId);

                calendarRealtimePublisher.publishDeleted(projectId, eventId);

                return ResponseEntity.ok(
                                ApiResponse.ok("Evento eliminado correctamente", null));
        }
}