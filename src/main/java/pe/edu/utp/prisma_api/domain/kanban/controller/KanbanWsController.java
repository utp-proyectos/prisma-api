package pe.edu.utp.prisma_api.domain.kanban.controller;

import java.security.Principal;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.kanban.dto.CreateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.DeleteKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.KanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.dto.UpdateKanbanDTO;
import pe.edu.utp.prisma_api.domain.kanban.services.KanbanService;
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
        UUID projectId = dto.getProjectId();

        KanbanDTO nuevoKanban = kanbanService.save(projectId, creatorId, dto);

        String destinationTopic = "/topic/project/" + projectId + "/kanbans";

        redisPublisher.publish(destinationTopic, nuevoKanban);
    }

    @MessageMapping("/kanban.update")
    public void updateKanban(@Valid @Payload UpdateKanbanDTO dto, Principal principal) {

        KanbanDTO kanbanActualizado = kanbanService.update(dto.getKanbanId(), dto)
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        String destinationTopic = "/topic/project/" + kanbanActualizado.getProjectId() + "/kanbans";

        redisPublisher.publish(destinationTopic, kanbanActualizado);
    }

    @MessageMapping("/kanban.delete")
    public void deleteKanban(
            @Payload DeleteKanbanDTO dto,
            Principal principal) {

        KanbanDTO kanban = kanbanService.findById(dto.getKanbanId())
                .orElseThrow(() -> new ResourceNotFoundException("Kanban no encontrado"));

        kanbanService.delete(dto.getKanbanId());

        String destinationTopic = "/topic/project/" + kanban.getProjectId() + "/kanbans";

        redisPublisher.publish(destinationTopic, dto);
    }
}