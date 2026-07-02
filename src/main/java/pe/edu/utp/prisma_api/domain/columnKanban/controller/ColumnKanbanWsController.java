package pe.edu.utp.prisma_api.domain.columnKanban.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.columnKanban.ColumnKanbanService;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ColumnKanbanDetailResponse;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ReorderColumnsDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class ColumnKanbanWsController {
        private final ColumnKanbanService columnService;
        private final RedisPublisher redisPublisher;

        @MessageMapping("/columnKanban.create")
        public void create(
                        @Payload @Valid CreateColumnKanbanDTO dto) {

                ColumnKanbanDetailResponse column = columnService.save(dto.getKanbanId(), dto);

                String topic = "/topic/" +
                                dto.getTeamId() + "/" +
                                dto.getProjectId() + "/" +
                                dto.getKanbanId() + "/columns";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.CREATE,
                                                column));
        }

        @MessageMapping("/columnKanban.reorder")
        public void reorderColumns(@Payload @Valid ReorderColumnsDTO dto) {

                columnService.reorderColumns(dto);

                String topic = "/topic/" +
                                dto.getTeamId() + "/" +
                                dto.getProjectId() + "/" +
                                dto.getKanbanId() + "/columns";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.REORDER,
                                                dto.getColumns()));
        }

}
