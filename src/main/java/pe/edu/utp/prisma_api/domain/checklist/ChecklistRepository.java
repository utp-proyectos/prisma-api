package pe.edu.utp.prisma_api.domain.checklist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<Checklist, UUID> {

    List<Checklist> findAllByTaskId(UUID taskId);

    @EntityGraph(attributePaths = {
            "task",
            "task.column",
            "task.column.kanban",
            "task.column.kanban.project",
            "task.column.kanban.project.team"
    })
    Optional<Checklist> findWithAllDetailsById(UUID id);

}