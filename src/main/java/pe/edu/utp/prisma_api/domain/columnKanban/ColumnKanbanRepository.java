package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ColumnKanbanRepository extends JpaRepository<ColumnKanban, UUID> {
    @Query("""
            SELECT COALESCE(MAX(c.position), -1)
            FROM ColumnKanban c
            WHERE c.kanban.id = :kanbanId
            """)
    Integer findLastPosition(UUID kanbanId);
}