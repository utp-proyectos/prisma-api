package pe.edu.utp.prisma_api.domain.checklist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, String> {
}