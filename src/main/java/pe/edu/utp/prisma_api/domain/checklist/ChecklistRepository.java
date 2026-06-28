package pe.edu.utp.prisma_api.domain.checklist;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<Checklist, UUID> {
}