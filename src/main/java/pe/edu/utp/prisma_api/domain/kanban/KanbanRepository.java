package pe.edu.utp.prisma_api.domain.kanban;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;

public interface KanbanRepository extends JpaRepository<Kanban, UUID> {

    List<Kanban> findByProjectId(UUID projectId);

    @EntityGraph(attributePaths = {
            "project",
            "project.team"
    })
    Optional<Kanban> findWithProjectAndTeamById(UUID id);

    // Trae tableros que sean públicos O privados pero que pertenezcan al usuario
    @Query("SELECT k FROM Kanban k WHERE k.project.id = :projectId AND (k.isPrivate = false OR k.creator.id = :userId)")
    List<Kanban> findByProjectIdAndAuthorizedUser(@Param("projectId") UUID projectId, @Param("userId") UUID userId);
}
