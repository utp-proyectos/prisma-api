package pe.edu.utp.prisma_api.domain.columnKanban;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnKanbanRepository extends JpaRepository<ColumnKanban, String> {
}