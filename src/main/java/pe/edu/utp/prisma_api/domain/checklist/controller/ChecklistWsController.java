package pe.edu.utp.prisma_api.domain.checklist.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.checklist.ChecklistService;
import pe.edu.utp.prisma_api.domain.checklist.dto.ChecklistDetailResponse;
import pe.edu.utp.prisma_api.domain.checklist.dto.CreateChecklistDTO;
import pe.edu.utp.prisma_api.domain.checklist.dto.DeleteChecklistDTO;
import pe.edu.utp.prisma_api.domain.checklist.dto.UpdateChecklistDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class ChecklistWsController {

        private final ChecklistService checklistService;
        private final RedisPublisher redisPublisher;

        @MessageMapping("/checklist.create")
        public void createChecklist(
                        @Payload @Valid CreateChecklistDTO dto) {

                ChecklistDetailResponse checklist = checklistService.save(dto.getTaskId(), dto);

                String topic = "/topic/" +
                                checklist.getTeamId() + "/" +
                                checklist.getProjectId() + "/" +
                                checklist.getKanbanId() + "/checklists";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.CREATE,
                                                checklist));
        }

        @MessageMapping("/checklist.update")
        public void updateChecklist(
                        @Payload @Valid UpdateChecklistDTO dto) {

                ChecklistDetailResponse checklist = checklistService.update(dto.getChecklistId(), dto);

                String topic = "/topic/" +
                                checklist.getTeamId() + "/" +
                                checklist.getProjectId() + "/" +
                                checklist.getKanbanId() + "/checklists";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.UPDATE,
                                                checklist));
        }

        @MessageMapping("/checklist.delete")
        public void deleteChecklist(
                        @Payload @Valid DeleteChecklistDTO dto) {

                ChecklistDetailResponse checklist = checklistService.findById(dto.getChecklistId())
                                .orElseThrow(() -> new ResourceNotFoundException("Checklist no encontrado"));

                checklistService.delete(dto.getChecklistId());

                String topic = "/topic/" +
                                checklist.getTeamId() + "/" +
                                checklist.getProjectId() + "/" +
                                checklist.getKanbanId() + "/checklists";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.DELETE,
                                                checklist));
        }

}
