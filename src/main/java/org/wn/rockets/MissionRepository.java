package org.wn.rockets;

import org.wn.rockets.dto.MissionSummaryDto;
import org.wn.rockets.entity.MissionStatus;
import org.wn.rockets.exception.AlreadyPresentInStoreException;
import org.wn.rockets.exception.NotFoundException;
import org.wn.rockets.exception.WrongStatusException;

import java.util.List;

/**
 * Repository used to save, retrieve and manipulate missions data.
 */
public interface MissionRepository {

    /**
     * Adds new mission with provided name. Status will be set to "Scheduled".
     * @param missionName   Name of a mission to be created.
     * @throws AlreadyPresentInStoreException if mission with given name is already present in the repository.
     */
    void addNewMission(String missionName) throws AlreadyPresentInStoreException;

    /**
     * Changes status of a mission stored in repository.
     * If changing status to "Pending", at least one rocket needs to be assigned to mission.
     * If changing status to "In Progress", at least one rocket needs to be assigned to mission,
     * and none of them should be "In Repair" stage.
     * If changing status to "Ended", avery rocket assigned to this mission will be unassigned.
     * @param missionName   Name of a mission to be updated.
     * @param status        Status of a mission to be set.
     * @throws NotFoundException when mission with given name does not exist in the repository.
     * @throws WrongStatusException when trying to change status, and precondition failed.
     */
    void changeMissionStatus(String missionName, MissionStatus status) throws NotFoundException, WrongStatusException;

    /**
     * Gets data about every mission in repository with rockets data assigned to their missions.
     * It's sorted by number of rockets assigned to mission in descending order.
     * If number of rockets assigned to missions is same, it will be sorted by name in descending, alphabetical order.
     * @return  List of missions summary.
     */
    List<MissionSummaryDto> getMissionsSummary();
}
