package pe.edu.utp.prisma_api.domain.task.controller;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanService;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.milestone.MilestoneService;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneDetailResponse;
import pe.edu.utp.prisma_api.domain.milestone.dto.MilestoneSummaryResponse;
import pe.edu.utp.prisma_api.domain.task.TaskService;
import pe.edu.utp.prisma_api.domain.task.dto.CreateTaskDTO;
import pe.edu.utp.prisma_api.domain.task.dto.DeleteTaskDTO;
import pe.edu.utp.prisma_api.domain.task.dto.ReorderTasksDTO;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDetailResponse;
import pe.edu.utp.prisma_api.domain.task.dto.TaskOperationResult;
import pe.edu.utp.prisma_api.domain.task.dto.UpdateTaskDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class TaskWsController {
        private final TaskService taskService;
        private final ColumnKanbanService columnService;
        private final MilestoneService milestoneService;
        private final RedisPublisher redisPublisher;

        @MessageMapping("/task.create")
        public void create(
                        @Payload @Valid CreateTaskDTO dto) {

                TaskOperationResult result = taskService.save(dto);

                publishMilestones(result.affectedMilestoneIds());

                publishTask(
                                dto.getTeamId(),
                                dto.getProjectId(),
                                dto.getKanbanId(),
                                WsAction.CREATE,
                                result.task());

        }

        @MessageMapping("/task.update")
        public void update(
                        @Payload @Valid UpdateTaskDTO dto) {

                TaskOperationResult result = taskService.update(dto);

                publishMilestones(result.affectedMilestoneIds());

                publishTask(
                                dto.getTeamId(),
                                dto.getProjectId(),
                                dto.getKanbanId(),
                                WsAction.UPDATE,
                                result.task());

        }

        @MessageMapping("/task.delete")
        public void delete(
                        @Payload @Valid DeleteTaskDTO dto) {

                TaskOperationResult result = taskService.delete(dto.getId());

                publishMilestones(result.affectedMilestoneIds());

                publishTask(
                                dto.getTeamId(),
                                dto.getProjectId(),
                                dto.getKanbanId(),
                                WsAction.DELETE,
                                result.task());

        }

        @MessageMapping("/task.reorder")
        public void reorderTasks(@Payload @Valid ReorderTasksDTO dto) {

                TaskOperationResult result = taskService.reorderTasks(dto);

                publishMilestones(result.affectedMilestoneIds());

                List<ColumnKanbanDetailResponse> columns = columnService.findAllByKanban(dto.getKanbanId());

                String topic = "/topic/" +
                                dto.getTeamId() + "/" +
                                dto.getProjectId() + "/" +
                                dto.getKanbanId() + "/tasks/reorder";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.REORDER,
                                                columns));

        }

        private void publishTask(UUID teamId,
                        UUID projectId,
                        UUID kanbanId,
                        WsAction action,
                        TaskDetailResponse task) {

                String topic = "/topic/" +
                                teamId + "/" +
                                projectId + "/" +
                                kanbanId + "/tasks";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(action, task));
        }

        private void publishMilestones(Set<UUID> milestoneIds) {

                for (UUID milestoneId : milestoneIds) {

                        MilestoneSummaryResponse summary = milestoneService.findSummaryById(milestoneId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Milestone no encontrado"));

                        if (summary != null) {

                                redisPublisher.publish(
                                                "/topic/"
                                                                + summary.getTeamId() + "/"
                                                                + summary.getProjectId() + "/"
                                                                + summary.getKanbanId()
                                                                + "/milestones",
                                                new WsResponse<>(
                                                                WsAction.UPDATE,
                                                                summary));
                        }

                        MilestoneDetailResponse detail = milestoneService.findById(milestoneId)
                                        .orElse(null);

                        if (detail != null) {

                                redisPublisher.publish(
                                                "/topic/"
                                                                + detail.getTeamId() + "/"
                                                                + detail.getProjectId() + "/"
                                                                + detail.getKanbanId()
                                                                + "/milestones/detail",
                                                new WsResponse<>(
                                                                WsAction.UPDATE,
                                                                detail));
                        }
                }
        }
}
