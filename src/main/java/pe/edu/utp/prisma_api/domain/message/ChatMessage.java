package pe.edu.utp.prisma_api.domain.message;

import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String sender;
  private String content;
  private String timestamp;
}