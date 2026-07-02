
package pe.edu.utp.prisma_api.domain.channel;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.channel.dto.ChannelResponse;
import pe.edu.utp.prisma_api.domain.channel.dto.CreateChannelRequest;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class ChannelWSController {
  private final ChannelService channelService;
  private final RedisPublisher redisPublisher;

  @MessageMapping("/channel.create")
  public void create(@Valid @Payload CreateChannelRequest request) {
    ChannelResponse channel = channelService.save(request);

    WsResponse<ChannelResponse> response = new WsResponse<>(WsAction.CREATE, channel);

    redisPublisher.publish("/topic/" + request.teamId() + "/" + request.projectId() + "/channels", response);
  }
}
