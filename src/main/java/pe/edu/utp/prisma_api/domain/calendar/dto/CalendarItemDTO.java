package pe.edu.utp.prisma_api.domain.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.utp.prisma_api.domain.calendar.enums.CalendarItemType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarItemDTO {

    private String id;

    private String title;

    private String start;

    private String end;

    private Boolean allDay;

    private CalendarItemType type;

    private String sourceId;//integer

    private Boolean editable;

    private String color;

    private String notes;
}

