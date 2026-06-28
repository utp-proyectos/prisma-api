package pe.edu.utp.prisma_api.domain.checklist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<Checklist, String> {
}