package pe.edu.utp.prisma_api.domain.invitation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.invitation.dto.AcceptInvitationResponse;
import pe.edu.utp.prisma_api.domain.invitation.dto.InviteMemberRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InvitationController {

  private final InvitationService invitationService;

  @PostMapping("/teams/{teamId}/invite")
  public ResponseEntity<ApiResponse<Void>> inviteMember(
      @PathVariable UUID teamId,
      @Valid @RequestBody InviteMemberRequest request,
      @AuthenticationPrincipal UUID userId) throws MessagingException {

    invitationService.inviteMember(teamId, request, userId);
    return ResponseEntity.ok(ApiResponse.ok("Invitación enviada", null));
  }

  @GetMapping("/invitations/accept")
  public ResponseEntity<ApiResponse<AcceptInvitationResponse>> acceptInvitation(
      @RequestParam UUID token,
      @AuthenticationPrincipal UUID userId) {
    return ResponseEntity
        .ok(ApiResponse.ok("Te uniste al equipo correctamente", invitationService.acceptInvitation(token, userId)));
  }

  @DeleteMapping("/invitations/{invitationId}")
  public ResponseEntity<ApiResponse<Void>> revokeInvitation(
      @PathVariable UUID invitationId,
      @AuthenticationPrincipal UUID userId) {

    invitationService.revokeInvitation(invitationId, userId);
    return ResponseEntity.ok(ApiResponse.ok("Invitación revocada", null));
  }
}