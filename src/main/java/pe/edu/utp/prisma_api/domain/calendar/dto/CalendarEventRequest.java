package pe.edu.utp.prisma_api.domain.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventRequest {

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean allDay;

    private String notes;
}