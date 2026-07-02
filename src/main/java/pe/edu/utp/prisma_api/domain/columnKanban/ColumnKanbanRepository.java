package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;

public interface ColumnKanbanRepository extends JpaRepository<ColumnKanban, UUID> {
    @Query("""
            SELECT COALESCE(MAX(c.position), -1)
            FROM ColumnKanban c
            WHERE c.kanban.id = :kanbanId
            """)
    Integer findLastPosition(UUID kanbanId);

    @Modifying(clearAutomatically = true) // <-- CRUCIAL
    @Query("UPDATE ColumnKanban c SET c.position = :position WHERE c.id = :id")
    void updatePosition(@Param("id") UUID id, @Param("position") Integer position);
}