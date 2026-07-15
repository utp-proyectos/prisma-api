package pe.edu.utp.prisma_api.domain.project;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @EntityGraph(attributePaths = { "kanbans" })
    List<Project> findByTeamId(UUID teamId);
}