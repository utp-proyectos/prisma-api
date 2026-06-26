package pe.edu.utp.prisma_api.domain.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    // Buscamos proyectos cuyo equipo tenga al usuario en su lista de miembros
    @Query("SELECT p FROM Team t JOIN t.projects p JOIN t.members m WHERE m.user.id = :userId")
    List<Project> findAllByUserId(String userId);

    // Para filtrar por fecha de creación
    @Query("SELECT p FROM Team t JOIN t.projects p JOIN t.members m WHERE m.user.id = :userId ORDER BY p.createdAt DESC")
    List<Project> findRecentProjectsByUserId(String userId);

    // Para obtener proyectos por equipo
    @Query("SELECT p FROM Team t JOIN t.projects p WHERE t.id = :teamId")
    List<Project> findByTeamId(String teamId);
}