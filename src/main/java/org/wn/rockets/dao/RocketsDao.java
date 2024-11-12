package org.wn.rockets.dao;

import org.wn.rockets.entity.RocketEntity;

import java.util.List;

/**
 * Interface for data access object for rocket entities. Contains abstraction for storing rockets data.
 */
public interface RocketsDao extends AbstractDao<RocketEntity>{

    /**
     * Gets every stored rocket entity that is assigned for mission with given name.
     * @param missionName   Name of a mission.
     * @return              List of rocket entities that are assigned to given mission.
     */
    List<RocketEntity> getRocketsByMissionName(String missionName);
}
