package pe.edu.utp.prisma_api.domain.kanban;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<Kanban, String> {

}
