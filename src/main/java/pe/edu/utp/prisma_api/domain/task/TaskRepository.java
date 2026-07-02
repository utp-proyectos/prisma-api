package pe.edu.utp.prisma_api.domain.task;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    // 1. Contar todas las tareas de un proyecto
    // Ruta: Project -> Kanbans -> Columns -> Tasks
    @Query("SELECT COUNT(t) FROM Project p JOIN p.kanbans k JOIN k.columns c JOIN c.tasks t WHERE p.id = :projectId")
    long countAllTasksByProjectId(@Param("projectId") UUID projectId);

    // 2. Contar solo tareas en columnas de tipo COMPLETED de un proyecto
    @Query("SELECT COUNT(t) FROM Project p JOIN p.kanbans k JOIN k.columns c JOIN c.tasks t WHERE p.id = :projectId AND c.type = pe.edu.utp.prisma_api.domain.columnKanban.enums.ColumnType.COMPLETED")
    long countCompletedTasksByProjectId(@Param("projectId") UUID projectId);

    // 3. Obtener próximas tareas (que no estén en columnas tipo COMPLETED) de un
    // proyecto
    @Query("SELECT t FROM Project p JOIN p.kanbans k JOIN k.columns c JOIN c.tasks t WHERE p.id = :projectId AND c.type <> :type ORDER BY t.dueDate ASC")
    List<Task> findUpcomingTasks(@Param("projectId") UUID projectId, @Param("type") ColumnType type);

    // 4. Filtrar tareas de un Kanban específico asignadas a un usuario
    @Query("SELECT t FROM ColumnKanban c JOIN c.tasks t JOIN t.assignments a WHERE c.id = :kanbanId AND a.user.id = :userId")
    List<Task> findByKanbanIdAndUserId(@Param("kanbanId") UUID kanbanId, @Param("userId") UUID userId);

    // 5. Obtener todas las tareas de un Kanban (Switch OFF)
    @Query("SELECT t FROM ColumnKanban c JOIN c.tasks t WHERE c.id = :kanbanId")
    List<Task> findAllByKanbanId(@Param("kanbanId") UUID kanbanId);

    // 6* Buscar tareas del proyecto actual, tenga dueDate y no esten en la columna COMPLETED
    @Query("""
        SELECT t FROM Project p
        JOIN p.kanbans k
        JOIN k.columns c
        JOIN c.tasks t
        WHERE p.id = :projectId
        AND t.dueDate IS NOT NULL
        AND t.dueDate BETWEEN :startDate AND :endDate
        AND c.type <> :completedType
        ORDER BY t.dueDate ASC
    """)
    List<Task> findDeadlinesByProjectAndDateRange(
            @Param("projectId") UUID projectId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("completedType") ColumnType completedType
    );
}