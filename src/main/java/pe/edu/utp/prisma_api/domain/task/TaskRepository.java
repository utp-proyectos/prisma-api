package pe.edu.utp.prisma_api.domain.task;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, UUID> {

        // 4. Filtrar tareas de un Kanban específico asignadas a un usuario
        @Query("SELECT t FROM ColumnKanban c JOIN c.tasks t JOIN t.assignments a WHERE c.id = :kanbanId AND a.user.id = :userId")
        List<Task> findByKanbanIdAndUserId(@Param("kanbanId") UUID kanbanId, @Param("userId") UUID userId);

        // 5. Obtener todas las tareas de un Kanban (Switch OFF)
        @Query("SELECT t FROM ColumnKanban c JOIN c.tasks t WHERE c.id = :kanbanId")
        List<Task> findAllByKanbanId(@Param("kanbanId") UUID kanbanId);

        // 6* Buscar tareas del proyecto actual, tenga dueDate y no esten en la columna
        // COMPLETED
        @Query("""
                            SELECT t FROM Project p
                            JOIN p.kanbans k
                            JOIN k.columns c
                            JOIN c.tasks t
                            WHERE p.id = :projectId
                            AND t.deadline IS NOT NULL
                            AND t.deadline BETWEEN :startDate AND :endDate
                            AND t.isCompleted = false
                            ORDER BY t.deadline ASC
                        """)
        List<Task> findDeadlinesByProjectAndDateRange(
                        @Param("projectId") UUID projectId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        int countByColumnId(UUID columnId);

        @Modifying(clearAutomatically = true)
        @Query("UPDATE Task t SET t.position = :position WHERE t.id = :id")
        void updatePosition(@Param("id") UUID id, @Param("position") Integer position);

        @Modifying
        @Query("UPDATE Task t SET t.milestone = null, t.deadline = null WHERE t.milestone.id = :milestoneId")
        void disassociateTasksFromMilestone(@Param("milestoneId") UUID milestoneId);

        @Query("SELECT t FROM Task t WHERE t.column.id = :columnId ORDER BY t.position")
        List<Task> findAllByColumnIdOrderByPosition(@Param("columnId") UUID columnId);

        @Query("""
                        select distinct t
                        from Task t
                        join fetch t.column c
                        join fetch c.kanban k
                        left join fetch t.assignments a
                        left join fetch a.user u
                        where u.id = :userId
                        """)
        List<Task> findTasksByUserIdWithKanban(@Param("userId") UUID userId);

        @EntityGraph(attributePaths = { "column", "column.kanban" })
        @Query("SELECT t FROM Task t " +
                        "JOIN t.column c " +
                        "JOIN c.kanban k " +
                        "JOIN t.assignments ta " +
                        "WHERE k.project.id = :projectId " +
                        "AND ta.user.id = :userId")
        List<Task> findTasksByProjectAndUser(@Param("projectId") UUID projectId, @Param("userId") UUID userId);
}