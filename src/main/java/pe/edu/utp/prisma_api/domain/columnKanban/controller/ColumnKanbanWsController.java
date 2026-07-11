package pe.edu.utp.prisma_api.domain.columnKanban.controller;

import java.util.List;

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
import pe.edu.utp.prisma_api.domain.columnKanban.dto.CreateColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.DeleteColumnKanbanDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.ReorderColumnsDTO;
import pe.edu.utp.prisma_api.domain.columnKanban.dto.UpdateColumnKanbanDTO;
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
                                column.getTeamId() + "/" +
                                column.getProjectId() + "/" +
                                column.getKanbanId() + "/columns";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.CREATE,
                                                column));
        }

        @MessageMapping("/columnKanban.update")
        public void update(
                        @Payload @Valid UpdateColumnKanbanDTO dto) {

                ColumnKanbanDetailResponse column = columnService.update(dto.getColumnId(), dto)
                                .orElseThrow(() -> new ResourceNotFoundException("Columna no encontrado"));

                String topic = "/topic/" +
                                column.getTeamId() + "/" +
                                column.getProjectId() + "/" +
                                column.getKanbanId() + "/columns";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.UPDATE,
                                                column));
        }

        @MessageMapping("/columnKanban.delete")
        public void delete(
                        @Payload @Valid DeleteColumnKanbanDTO dto) {

                ColumnKanbanDetailResponse column = columnService.findById(dto.getColumnId())
                                .orElseThrow(() -> new ResourceNotFoundException("Columna no encontrada"));

                columnService.delete(dto.getColumnId());

                String topic = "/topic/" +
                                column.getTeamId() + "/" +
                                column.getProjectId() + "/" +
                                column.getKanbanId() + "/columns";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.DELETE,
                                                column));
        }

        @MessageMapping("/columnKanban.reorder")
        public void reorderColumns(@Payload @Valid ReorderColumnsDTO dto) {

                System.out.println("Entró a reorderColumns");

                columnService.reorderColumns(dto);

                List<ColumnKanbanDetailResponse> columns = columnService.findAllByKanban(dto.getKanbanId());

                String topic = "/topic/" +
                                dto.getTeamId() + "/" +
                                dto.getProjectId() + "/" +
                                dto.getKanbanId() + "/columns/reorder";

                redisPublisher.publish(
                                topic,
                                new WsResponse<>(
                                                WsAction.REORDER,
                                                columns));
        }

}
