package pe.edu.utp.prisma_api.domain.checklist;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, UUID> {
}