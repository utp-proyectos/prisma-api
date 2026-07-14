package pe.edu.utp.prisma_api.domain.checklistItem.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.CreateChecklistItemDTO;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.DeleteChecklistItemDTO;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.UpdateChecklistItemDTO;
import pe.edu.utp.prisma_api.domain.checklistItem.ChecklistItemService;
import pe.edu.utp.prisma_api.domain.checklistItem.dto.ChecklistItemResponse;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class ChecklistItemWsController {

    private final ChecklistItemService checklistItemService;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/checklistItem.create")
    public void createChecklistItem(@Payload @Valid CreateChecklistItemDTO dto) {
        ChecklistItemResponse checklistItem = checklistItemService.save(dto.getChecklistId(), dto);
        publishToRedis(WsAction.CREATE, checklistItem);
    }

    @MessageMapping("/checklistItem.update")
    public void updateChecklistItem(@Payload @Valid UpdateChecklistItemDTO dto) {
        ChecklistItemResponse checklistItem = checklistItemService.update(dto.getChecklistItemId(), dto);
        publishToRedis(WsAction.UPDATE, checklistItem);
    }

    @MessageMapping("/checklistItem.delete")
    public void deleteChecklistItem(@Payload @Valid DeleteChecklistItemDTO dto) {
        ChecklistItemResponse checklistItem = checklistItemService.delete(dto.getChecklistItemId());
        publishToRedis(WsAction.DELETE, checklistItem);
    }

    private void publishToRedis(WsAction action, ChecklistItemResponse item) {
        String topic = String.format("/topic/%s/%s/%s/checklistItems",
                item.getTeamId(),
                item.getProjectId(),
                item.getKanbanId());

        redisPublisher.publish(topic, new WsResponse<>(action, item));
    }
}