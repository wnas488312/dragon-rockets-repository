package org.wn.rockets.dao;

import org.wn.rockets.entity.RocketEntity;
import org.wn.rockets.entity.RocketStatus;

import java.util.List;

/**
 * Interface for data access object for rocket entities. Contains abstraction for storing rockets data.
 */
public non-sealed interface RocketsDao extends AbstractDao<RocketEntity, String, RocketStatus>{

    /**
     * Changes or sets name of a mission, that given rocket will be assigned to.
     * @param rocketName    Name of a rocket to be changed.
     * @param missionName   Name of a mission.
     */
    void assignRocketToMission(String rocketName, String missionName);

    /**
     * Gets every stored rocket entity that is assigned for mission with given name.
     * @param missionName   Name of a mission.
     * @return              List of rocket entities that are assigned to given mission.
     */
    List<RocketEntity> getRocketsByMissionName(String missionName);
}
