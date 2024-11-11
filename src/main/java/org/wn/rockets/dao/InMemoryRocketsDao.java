package org.wn.rockets.dao;

import lombok.Getter;
import org.wn.rockets.entity.RocketEntity;
import org.wn.rockets.entity.RocketStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRocketsDao implements RocketsDao{
    private Map<String, RocketEntity> store = new HashMap<>();

    @Getter
    private static final InMemoryRocketsDao instance = new InMemoryRocketsDao();

    private InMemoryRocketsDao() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNew(RocketEntity rocket) {
        // TODO: actual implementation.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeStatus(String identifier, RocketStatus status) {
        // TODO: actual implementation.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignRocketToMission(String rocketName, String missionName) {
        // TODO: actual implementation.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RocketEntity> getRocketsByMissionName(String missionName) {
        // TODO: actual implementation.
        return List.of();
    }
}
