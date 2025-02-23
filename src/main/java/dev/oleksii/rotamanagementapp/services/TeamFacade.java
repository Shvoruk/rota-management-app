package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.domain.entities.User;

import java.util.Set;
import java.util.UUID;

/**
 * Service interface that defines the contract for team-related operations.
 * <p>
 * This interface provides methods for:
 * <ul>
 *   <li>Retrieving a single team by its identifier.</li>
 *   <li>Retrieving all teams associated with a user.</li>
 *   <li>Fetching the schedule of a given team.</li>
 *   <li>Fetching all members of a team.</li>
 *   <li>Creating a new team along with its mandatory schedule and initial membership.</li>
 *   <li>Deleting an existing team.</li>
 *   <li>Joining a team as a member.</li>
 *   <li>Leaving a team.</li>
 * </ul>
 */
public interface TeamFacade {

    /**
     * Retrieves the team details for the given team ID by:
     * <ul>
     *   <li>Querying the underlying team service for the team entity.</li>
     *   <li>Mapping the team entity to a {@link TeamDto}.</li>
     * </ul>
     *
     * @param teamId The unique identifier of the team.
     * @return A {@link TeamDto} representing the team.
     */
    TeamDto getTeam(UUID teamId);

    /**
     * Retrieves all teams associated with the given user by:
     * <ul>
     *   <li>Extracting the user's memberships.</li>
     *   <li>Mapping each membership to its corresponding team entity.</li>
     *   <li>Converting the set of team entities to a set of {@link TeamDto} objects.</li>
     * </ul>
     *
     * @param user The user whose teams are to be retrieved.
     * @return A set of {@link TeamDto} representing all teams the user belongs to.
     */
    Set<TeamDto> getAllTeams(User user);

    /**
     * Retrieves the schedule associated with the specified team by:
     * <ul>
     *   <li>Fetching the schedule entity for the team via the schedule service.</li>
     *   <li>Mapping the schedule entity to a {@link ScheduleDto}.</li>
     * </ul>
     *
     * @param teamId The unique identifier of the team.
     * @return A {@link ScheduleDto} representing the team's schedule.
     */
    ScheduleDto getTeamSchedule(UUID teamId);

    /**
     * Retrieves all members of the specified team by:
     * <ul>
     *   <li>Querying the membership service for all memberships linked to the team.</li>
     *   <li>Mapping the membership entities to a set of {@link MemberDto} objects.</li>
     * </ul>
     *
     * @param teamId The unique identifier of the team.
     * @return A set of {@link MemberDto} representing the team members.
     */
    Set<MemberDto> getAllTeamMembers(UUID teamId);

    /**
     * Creates a new team with the given name for the specified user by:
     * <ul>
     *   <li>Building a new team entity with the provided name.</li>
     *   <li>Creating a mandatory schedule associated with the team.</li>
     *   <li>Adding the user as a manager member of the team.</li>
     *   <li>Persisting the new team entity and returning its DTO representation.</li>
     * </ul>
     *
     * @param user The user who is creating the team.
     * @param name The name of the team.
     * @return A {@link TeamDto} representing the newly created team.
     */
    TeamDto createTeam(User user, String name);

    /**
     * Deletes the team identified by the given team ID by:
     * <ul>
     *   <li>Delegating the deletion operation to the underlying team service.</li>
     * </ul>
     *
     * @param teamId The unique identifier of the team to be deleted.
     */
    void deleteTeam(UUID teamId);

    /**
     * Adds the specified user to the team identified by the given team ID by:
     * <ul>
     *   <li>Retrieving the team entity via the team service.</li>
     *   <li>Building a new membership for the user with an employee role.</li>
     *   <li>Adding the membership to the team and persisting the updated team entity.</li>
     *   <li>Returning the updated team DTO.</li>
     * </ul>
     *
     * @param user   The user who wishes to join the team.
     * @param teamId The unique identifier of the team.
     * @return A {@link TeamDto} representing the team after the user has joined.
     */
    TeamDto joinTeam(User user, UUID teamId);

    /**
     * Removes the specified user from the team identified by the given team ID by:
     * <ul>
     *   <li>Delegating the membership deletion operation to the membership service.</li>
     * </ul>
     *
     * @param user   The user who wishes to leave the team.
     * @param teamId The unique identifier of the team.
     */
    void leaveTeam(User user, UUID teamId);
}