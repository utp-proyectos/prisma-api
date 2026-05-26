package pe.edu.utp.prisma_api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class InvitationExpiredException extends RuntimeException {
  public InvitationExpiredException() {
    super("La invitación ha expirado");
  }
}
