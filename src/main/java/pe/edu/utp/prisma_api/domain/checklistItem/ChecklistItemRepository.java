package pe.edu.utp.prisma_api.domain.checklistItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, UUID> {

    List<ChecklistItem> findAllByChecklistId(UUID checklistId);

    @EntityGraph(attributePaths = {
            "checklist",
            "checklist.task",
            "checklist.task.column",
            "checklist.task.column.kanban",
            "checklist.task.column.kanban.project",
            "checklist.task.column.kanban.project.team"
    })
    Optional<ChecklistItem> findWithAllDetailsById(UUID id);
}