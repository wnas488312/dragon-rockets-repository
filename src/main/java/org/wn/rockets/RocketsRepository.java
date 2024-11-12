package org.wn.rockets;

import org.wn.rockets.entity.RocketStatus;
import org.wn.rockets.exception.AlreadyPresentInStoreException;
import org.wn.rockets.exception.NotFoundException;

import java.util.List;

/**
 * Repository used to save, retrieve and manipulate rockets data.
 */
public interface RocketsRepository {

    /**
     * Adds new rocket with provided name. Status will be set to "On ground".
     * @param rocketName   Name of a rocket to be created.
     * @throws AlreadyPresentInStoreException if rocket with given name is already present in the repository.
     */
    void addNewRocket(String rocketName) throws AlreadyPresentInStoreException;

    /**
     * Assigns a given rocket (found by provided name) to mission.
     * @param rocketName    Name of a rocket to be assigned.
     * @param missionName   Name of a mission that rocket will be assigned to.
     * @throws NotFoundException if rocket or mission for given name is not present in repository.
     */
    void assignRocketToMission(String rocketName, String missionName) throws NotFoundException;

    /**
     * Assigns a multiple rockets (found by provided name) to mission.
     * @param rocketsNames      List of names of a rockets to be assigned.
     * @param missionName       Name of a mission that rockets will be assigned to.
     * @throws NotFoundException if mission for given name is not present in repository.
     */
    void assignRocketsToMission(List<String> rocketsNames, String missionName) throws NotFoundException;

    /**
     * Changes status of a rocket stored in repository.
     * @param rocketName    Name of a rocket to be updated.
     * @param status        Status of a rocket to be set.
     * @throws NotFoundException when rocket with given name does not exist in the repository.
     */
    void changeRocketStatus(String rocketName, RocketStatus status) throws NotFoundException;
}
