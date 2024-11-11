package org.wn.rockets.dao;

import org.wn.rockets.entity.MissionEntity;
import org.wn.rockets.entity.MissionStatus;

import java.util.List;

/**
 * Interface for data access object for mission entities. Contains abstraction for storing missions data.
 */
public non-sealed interface MissionsDao extends AbstractDao<MissionEntity, String, MissionStatus> {

    /**
     * Gets every stored mission entity.
     * @return List of stored mission entities.
     */
    List<MissionEntity> getAll();
}
