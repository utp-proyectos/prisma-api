package pe.edu.utp.prisma_api.domain.task.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.task.TaskService;
import pe.edu.utp.prisma_api.domain.task.dto.CreateTaskDTO;
import pe.edu.utp.prisma_api.domain.task.dto.ReorderTasksDTO;
import pe.edu.utp.prisma_api.domain.task.dto.TaskDetailResponse;
import pe.edu.utp.prisma_api.domain.task.dto.UpdateTaskDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class TaskWsController {
        private final TaskService taskService;
        private final RedisPublisher redisPublisher;

        @MessageMapping("/task.create")
        public void create(
                        @Payload @Valid CreateTaskDTO dto) {

                TaskDetailResponse task = taskService.save(dto);

                String topic = "/topic/" +
                                dto.getTeamId() + "/" +
                                dto.getProjectId() + "/" +
                                dto.getKanbanId() + "/tasks";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.CREATE,
                                                task));
        }

        @MessageMapping("/task.update")
        public void update(
                        @Payload @Valid UpdateTaskDTO dto) {

                TaskDetailResponse task = taskService.update(dto);

                String topic = "/topic/" +
                                dto.getTeamId() + "/" +
                                dto.getProjectId() + "/" +
                                dto.getKanbanId() + "/tasks";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.UPDATE,
                                                task));
        }

        @MessageMapping("/task.reorder")
        public void reorderTasks(@Payload @Valid ReorderTasksDTO dto) {

                taskService.reorderTasks(dto);

                String topic = "/topic/" +
                                dto.getTeamId() + "/" +
                                dto.getProjectId() + "/" +
                                dto.getKanbanId() + "/tasks";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.REORDER,
                                                dto));
        }
}
