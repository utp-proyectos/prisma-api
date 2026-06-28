package pe.edu.utp.prisma_api.domain.team;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.project.dto.ProjectResponse;
import pe.edu.utp.prisma_api.domain.team.dto.CreateTeamRequest;
import pe.edu.utp.prisma_api.domain.team.dto.TeamDetailResponse;
import pe.edu.utp.prisma_api.domain.team.dto.TeamMemberResponse;
import pe.edu.utp.prisma_api.domain.team.dto.TeamSummaryResponse;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class TeamService {
	private final TeamRepository teamRepository;
	private final TeamMemberRepository teamMemberRepository;
	private final UserRepository userRepository;

	public TeamSummaryResponse createTeam(CreateTeamRequest request, UUID userId) {
		User owner = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

		Team team = new Team();
		team.setName(request.getName());
		teamRepository.save(team);

		TeamMember member = new TeamMember();
		member.setUser(owner);
		member.setTeam(team);
		member.setRole(TeamRole.OWNER);
		teamMemberRepository.save(member);

		return new TeamSummaryResponse(team.getId(), team.getName());
	}

	public List<TeamSummaryResponse> getMyTeams(UUID userId) {
		System.out.println("------------------------------------");
		System.out.println("mis equipos");
		System.out.println("------------------------------------");
		return teamRepository.findByMembersUserId(userId)
				.stream()
				.map(t -> new TeamSummaryResponse(t.getId(), t.getName()))
				.toList();
	}

	public TeamDetailResponse getTeamById(UUID teamId, UUID userId) {
		System.out.println("------------------------------------");
		System.out.println("team by id");
		System.out.println("------------------------------------");
		if (!teamMemberRepository.existsByUserIdAndTeamId(userId, teamId)) {
			throw new ResourceNotFoundException("No perteneces a este equipo");
		}
		System.out.println("------------------------------------");
		System.out.println("valido usuari");
		System.out.println("------------------------------------");
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado"));
		System.out.println("------------------------------------");
		System.out.println("se cargo el eqipo");
		System.out.println("------------------------------------");
		List<TeamMemberResponse> members = team.getMembers().stream()
				.map(tm -> new TeamMemberResponse(tm.getUser().getId(), tm.getUser().getName(),
						tm.getUser().getLastName(), tm.getUser().getEmail(),
						tm.getUser().getUsername(), tm.getUser().getAvatar(),
						tm.getRole()))
				.toList();

		System.out.println("------------------------------------");
		System.out.println("se cargo los miembros");
		System.out.println("------------------------------------");

		List<ProjectResponse> projects = team.getProjects().stream()
				.map(p -> new ProjectResponse(p.getId(), p.getName(), p.getDescription(),
						p.getCreatedAt()))
				.toList();

		System.out.println("------------------------------------");
		System.out.println("se cargo los proyectos");
		System.out.println("------------------------------------");

		return new TeamDetailResponse(team.getId(), team.getName(), members, projects);

	}
}
