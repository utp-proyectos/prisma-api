package pe.edu.utp.prisma_api.domain.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.prisma_api.domain.calendar.enums.CalendarWsAction;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarWsMessage {

    private CalendarWsAction action;

    private UUID projectId;

    private String sourceId;

    private CalendarItemDTO item;
}