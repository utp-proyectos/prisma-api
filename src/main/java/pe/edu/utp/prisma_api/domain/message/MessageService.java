package pe.edu.utp.prisma_api.domain.message;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.channel.Channel;
import pe.edu.utp.prisma_api.domain.channel.ChannelRepository;
import pe.edu.utp.prisma_api.domain.message.dto.CreateMessageRequest;
import pe.edu.utp.prisma_api.domain.message.dto.MessageResponse;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;

  MessageResponse save(CreateMessageRequest request, UUID userId) {
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new ResourceNotFoundException("Canal no encontrado"));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    Message message = new Message();

    message.setContent(request.content());
    message.setChannel(channel);
    message.setSender(user);

    messageRepository.save(message);

    return new MessageResponse(
        message.getId(),
        message.getContent(),
        user.getUsername(),
        user.getAvatar(),
        user.getId(),
        message.getCreatedAt());
  }
}
