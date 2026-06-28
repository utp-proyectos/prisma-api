package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<Kanban, UUID> {
    List<Kanban> findByProjectId(UUID projectId);
}
