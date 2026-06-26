package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<Kanban, String> {
    List<Kanban> findByProjectId(String projectId);
}
