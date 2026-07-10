package pe.edu.utp.prisma_api.domain.milestone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {
    List<Milestone> findAllByKanbanId(UUID kanbanId);

    @EntityGraph(attributePaths = {
            "kanban",
            "kanban.project",
            "kanban.project.team"
    })
    Optional<Milestone> findWithKanbanAndProjectAndTeamById(UUID id);
}
