package pe.edu.utp.prisma_api.domain.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utp.prisma_api.domain.calendar.model.CalendarEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, UUID> {

    List<CalendarEvent> findByProject_IdAndActiveTrueOrderByStartDateAsc(UUID projectId);

    Optional<CalendarEvent> findByIdAndProject_IdAndActiveTrue(UUID id, UUID projectId);

    @Query("SELECT e FROM CalendarEvent e " +
            "WHERE e.project.id = :projectId " +
            "AND e.active = true " +
            "AND e.startDate <= :endDate " +
            "AND e.endDate >= :startDate " +
            "ORDER BY e.startDate ASC")
    List<CalendarEvent> findByProjectAndDateRange(
            @Param("projectId") UUID projectId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}