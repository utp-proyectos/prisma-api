package pe.edu.utp.prisma_api.domain.milestone;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {
    List<Milestone> findAllByKanbanId(UUID kanbanId);
}
