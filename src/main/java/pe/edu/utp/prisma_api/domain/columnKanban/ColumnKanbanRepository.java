package pe.edu.utp.prisma_api.domain.columnKanban;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnKanbanRepository extends JpaRepository<ColumnKanban, UUID> {
}