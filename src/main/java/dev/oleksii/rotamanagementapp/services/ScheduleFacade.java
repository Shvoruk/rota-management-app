package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.CreateShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberShiftDto;
import dev.oleksii.rotamanagementapp.domain.dtos.ShiftDto;
import dev.oleksii.rotamanagementapp.domain.entities.User;

import java.util.Set;
import java.util.UUID;

/**
 * Service interface that defines the contract for schedule and shift-related operations.
 * <p>
 * This interface provides methods for:
 * <ul>
 *   <li>Retrieving a specific shift within a team.</li>
 *   <li>Retrieving all shift assignments (member shifts) for a given team and user.</li>
 *   <li>Creating a new shift for a team based on provided details.</li>
 *   <li>Deleting an existing shift from a team.</li>
 *   <li>Assigning a member to a shift using assignment details.</li>
 *   <li>Unassigning a member from a shift.</li>
 * </ul>
 */
public interface ScheduleFacade {

    /**
     * Retrieves the details of a specific shift by:
     * <ul>
     *   <li>Validating that the shift belongs to the team identified by {@code teamId}.</li>
     *   <li>Mapping the shift entity to a {@link ShiftDto} for data transfer.</li>
     * </ul>
     *
     * @param teamId  The unique identifier of the team.
     * @param shiftId The unique identifier of the shift.
     * @return A {@link ShiftDto} representing the shift details.
     */
    ShiftDto getShift(UUID teamId, UUID shiftId);

    /**
     * Retrieves all member shift assignments for the given team by:
     * <ul>
     *   <li>Validating that the user is associated with the team.</li>
     *   <li>Fetching all member shift entities related to the team.</li>
     *   <li>Mapping the collection of member shift entities to a set of {@link MemberShiftDto} objects.</li>
     * </ul>
     *
     * @param user   The user for whom the member shifts are to be retrieved.
     * @param teamId The unique identifier of the team.
     * @return A set of {@link MemberShiftDto} representing all member shifts for the team.
     */
    Set<MemberShiftDto> getAllMemberShifts(User user, UUID teamId);

    /**
     * Creates a new shift for the specified team by:
     * <ul>
     *   <li>Validating that the team exists and the user is authorized.</li>
     *   <li>Building a new shift entity using details provided in the {@link CreateShiftRequest}.</li>
     *   <li>Associating the new shift with the team's schedule.</li>
     *   <li>Persisting the new shift and mapping it to a {@link ShiftDto} for return.</li>
     * </ul>
     *
     * @param teamId  The unique identifier of the team.
     * @param request The details required to create a new shift.
     * @return A {@link ShiftDto} representing the newly created shift.
     */
    ShiftDto createShift(UUID teamId, CreateShiftRequest request);

    /**
     * Deletes the specified shift from the team by:
     * <ul>
     *   <li>Validating that the shift belongs to the team identified by {@code teamId}.</li>
     *   <li>Delegating the deletion operation to the underlying schedule service.</li>
     * </ul>
     *
     * @param teamId  The unique identifier of the team.
     * @param shiftId The unique identifier of the shift to be deleted.
     */
    void deleteShift(UUID teamId, UUID shiftId);

    /**
     * Assigns a member to a shift by:
     * <ul>
     *   <li>Validating that the shift belongs to the team identified by {@code teamId}.</li>
     *   <li>Building a new member shift assignment using details from the {@link AssignShiftRequest}.</li>
     *   <li>Persisting the new member shift assignment.</li>
     *   <li>Mapping the member shift entity to a {@link MemberShiftDto} for return.</li>
     * </ul>
     *
     * @param teamId  The unique identifier of the team.
     * @param shiftId The unique identifier of the shift.
     * @param request The details for assigning the shift to a member.
     * @return A {@link MemberShiftDto} representing the assigned shift details.
     */
    MemberShiftDto assignShift(UUID teamId, UUID shiftId, AssignShiftRequest request);

    /**
     * Unassigns a member from a shift by:
     * <ul>
     *   <li>Validating that the shift and member shift assignment exist for the team.</li>
     *   <li>Delegating the unassignment operation to the underlying service responsible for member shifts.</li>
     * </ul>
     *
     * @param teamId        The unique identifier of the team.
     * @param shiftId       The unique identifier of the shift.
     * @param memberShiftId The unique identifier of the member shift assignment to be removed.
     */
    void unassignShift(UUID teamId, UUID shiftId, UUID memberShiftId);
}
