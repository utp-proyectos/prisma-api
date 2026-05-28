package pe.edu.utp.prisma_api.domain.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    // Contar todas las tareas de todos los kanbans de un proyecto
    long countByColumn_Kanban_Project_Id(String projectId);

    // Contar solo las tareas que están en columnas de tipo COMPLETED dentro de un
    // proyecto
    long countByColumn_Kanban_Project_IdAndColumn_Type(String projectId, ColumnType type);

    // Obtener las próximas tareas (para la segunda tarjeta de tu imagen)
    List<Task> findByColumn_Kanban_Project_IdAndColumn_TypeIsNotOrderByDueDateAsc(
            String projectId,
            ColumnType type);

    // Filtra las tareas de un tablero específico que pertenecen a un usuario
    // concreto
    @Query("SELECT t FROM Task t JOIN t.assignments a WHERE t.column.kanban.id = :kanbanId AND a.user.id = :userId")
    List<Task> findByKanbanIdAndUserId(@Param("kanbanId") String kanbanId, @Param("userId") String userId);

    // Consulta normal para cuando el switch está en "OFF" (Ver todo)
    List<Task> findByColumn_Kanban_Id(String kanbanId);
}