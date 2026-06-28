package pe.edu.utp.prisma_api.domain.invitation;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.common.exception.InvalidTokenException;
import pe.edu.utp.prisma_api.common.exception.InvitationExpiredException;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.common.exception.UnauthorizedException;
import pe.edu.utp.prisma_api.domain.invitation.dto.AcceptInvitationResponse;
import pe.edu.utp.prisma_api.domain.invitation.dto.InviteMemberRequest;
import pe.edu.utp.prisma_api.domain.team.Team;
import pe.edu.utp.prisma_api.domain.team.TeamMember;
import pe.edu.utp.prisma_api.domain.team.TeamMemberRepository;
import pe.edu.utp.prisma_api.domain.team.TeamRepository;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;
import pe.edu.utp.prisma_api.infraestructure.mail.MailService;

@Service
@RequiredArgsConstructor
public class InvitationService {
  private final InvitationRepository invitationRepository;
  private final TeamRepository teamRepository;
  private final TeamMemberRepository teamMemberRepository;
  private final UserRepository userRepository;
  private final MailService mailService;

  public void inviteMember(UUID teamId, InviteMemberRequest request, UUID inviterId) throws MessagingException {
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));

    User inviter = userRepository.findById(inviterId)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    boolean alreadyInvited = invitationRepository
        .findByEmailAndTeamId(request.getEmail(), teamId)
        .filter(inv -> !inv.isAccepted())
        .isPresent();

    if (alreadyInvited) {
      throw new UnauthorizedException("Este usuario ya tiene una invitación pendiente");
    }

    Invitation invitation = new Invitation();
    invitation.setEmail(request.getEmail());
    invitation.setTeam(team);
    invitation.setInvitedBy(inviter);
    invitation.setRole(request.getRole());
    invitation.setToken(UUID.randomUUID());
    invitation.setExpiresAt(LocalDateTime.now().plusHours(48));

    invitationRepository.save(invitation);

    mailService.sendInvitationEmail(
        request.getEmail(),
        team.getName(),
        invitation.getToken());
  }

  public AcceptInvitationResponse acceptInvitation(UUID token, UUID userId) {
    Invitation invitation = invitationRepository.findByToken(token)
        .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

    if (invitation.isAccepted()) {
      throw new InvalidTokenException("Esta invitación ya fue utilizada");
    }

    if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvitationExpiredException();
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    if (!user.getEmail().equalsIgnoreCase(invitation.getEmail())) {
      throw new UnauthorizedException("Esta invitación no corresponde a tu cuenta");
    }

    TeamMember member = new TeamMember();
    member.setUser(user);
    member.setTeam(invitation.getTeam());
    member.setRole(invitation.getRole());
    teamMemberRepository.save(member);

    invitation.setAccepted(true);
    invitationRepository.save(invitation);

    return new AcceptInvitationResponse(invitation.getTeam().getId());
  }

  public void revokeInvitation(UUID invitationId, UUID requesterId) {
    Invitation invitation = invitationRepository.findById(invitationId)
        .orElseThrow(() -> new ResourceNotFoundException("Invitación no encontrada"));

    boolean isInviter = invitation.getInvitedBy().getId().equals(requesterId);
    boolean isAdmin = teamMemberRepository
        .findByUserIdAndTeamId(requesterId, invitation.getTeam().getId())
        .map(tm -> tm.getRole() == TeamRole.EDITOR || tm.getRole() == TeamRole.OWNER)
        .orElse(false);

    if (!isInviter && !isAdmin) {
      throw new UnauthorizedException("No tienes permiso para revocar esta invitación");
    }

    invitationRepository.delete(invitation);
  }
}
