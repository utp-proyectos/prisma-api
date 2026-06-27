package pe.edu.utp.prisma_api.domain.team.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.team.Team;
import pe.edu.utp.prisma_api.domain.team.TeamMapper;
import pe.edu.utp.prisma_api.domain.team.TeamMember;
import pe.edu.utp.prisma_api.domain.team.TeamMemberRepository;
import pe.edu.utp.prisma_api.domain.team.TeamRepository;
import pe.edu.utp.prisma_api.domain.team.dto.CreateTeamDTO;
import pe.edu.utp.prisma_api.domain.team.dto.TeamDTO;
import pe.edu.utp.prisma_api.domain.team.dto.UpdateTeamDTO;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;

    private final TeamMemberRepository teamMemberRepository;

    private final TeamMapper mapper;

    @Override
    public List<TeamDTO> findAll() {
        return mapper.toDto(teamRepository.findAll());
    }

    @Override
    public Optional<TeamDTO> findById(String id) {
        return teamRepository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    public TeamDTO save(CreateTeamDTO dto, String ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Team team = mapper.toEntity(dto);

        TeamMember member = new TeamMember();
        member.setUser(owner);
        member.setTeam(team);
        member.setRole(TeamRole.OWNER);
        member.setJoinedAt(LocalDate.now());

        team.getMembers().add(member);

        return mapper.toDto(
                teamRepository.save(team));
    }

    @Override
    public Optional<TeamDTO> update(String id,
            UpdateTeamDTO dto) {

        return teamRepository.findById(id)
                .map(team -> {

                    mapper.update(dto, team);

                    return mapper.toDto(
                            teamRepository.save(team));
                });
    }

    @Override
    public void delete(String id) {
        teamRepository.deleteById(id);
    }

    @Override
    public List<TeamDTO> findByUser(String userId) {

        return mapper.toDto(
                teamRepository.findByMembersUserId(userId));
    }

    @Override
    public void updateMemberRole(
            String teamId,
            String adminId,
            String targetUserId,
            TeamRole role) {

        TeamMember admin = teamMemberRepository
                .findByUserIdAndTeamId(adminId, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("No perteneces al equipo"));

        if (admin.getRole() != TeamRole.ADMIN &&
                admin.getRole() != TeamRole.OWNER) {

            throw new IllegalArgumentException(
                    "No tienes permisos para cambiar roles");
        }

        TeamMember target = teamMemberRepository
                .findByUserIdAndTeamId(targetUserId, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        target.setRole(role);

        teamMemberRepository.save(target);
    }
}