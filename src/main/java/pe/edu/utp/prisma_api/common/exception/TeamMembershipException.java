package pe.edu.utp.prisma_api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TeamMembershipException extends RuntimeException {
  public TeamMembershipException(String message) {
    super(message);
  }
}