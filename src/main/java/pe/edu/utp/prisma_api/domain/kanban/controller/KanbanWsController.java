package pe.edu.utp.prisma_api.domain.kanban.controller;

import java.security.Principal;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.kanban.KanbanService;
import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.DeleteKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class KanbanWsController {

        private final KanbanService kanbanService;
        private final RedisPublisher redisPublisher;

        @MessageMapping("/kanban.create")
        public void createKanban(
                        @Valid @Payload CreateKanbanDTO dto,
                        Principal principal) {

                UUID creatorId = UUID.fromString(principal.getName());

                KanbanDTO nuevoKanban = kanbanService.save(dto.getProjectId(), creatorId, dto);

                String destinationTopic = "/topic/" + nuevoKanban.getTeamId() + "/" + nuevoKanban.getProjectId()
                                + "/kanbans";

                redisPublisher.publish(destinationTopic, new WsResponse<>(
                                WsAction.CREATE,
                                nuevoKanban));
        }

        @MessageMapping("/kanban.update")
        public void updateKanban(@Valid @Payload UpdateKanbanDTO dto) {

                KanbanDTO kanbanActualizado = kanbanService.update(dto.getKanbanId(), dto)
                                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

                String destinationTopic = "/topic/" + kanbanActualizado.getTeamId() + "/"
                                + kanbanActualizado.getProjectId()
                                + "/kanbans";

                redisPublisher.publish(destinationTopic, new WsResponse<>(
                                WsAction.UPDATE,
                                kanbanActualizado));
        }

        @MessageMapping("/kanban.delete")
        public void deleteKanban(
                        @Payload DeleteKanbanDTO dto,
                        Principal principal) {

                UUID creatorId = UUID.fromString(principal.getName());

                KanbanDetailResponse kanban = kanbanService.findById(dto.getKanbanId(), creatorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

                kanbanService.delete(dto.getKanbanId());

                String destinationTopic = "/topic/" + kanban.getTeamId() + "/"
                                + kanban.getProjectId()
                                + "/kanbans";

                redisPublisher.publish(destinationTopic, new WsResponse<>(
                                WsAction.DELETE,
                                kanban));
        }
}