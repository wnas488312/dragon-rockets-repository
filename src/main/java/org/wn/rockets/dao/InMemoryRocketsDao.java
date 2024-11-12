package org.wn.rockets.dao;

import lombok.Getter;
import org.wn.rockets.entity.RocketEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRocketsDao implements RocketsDao{
    private final Map<String, RocketEntity> store;

    @Getter
    private static final InMemoryRocketsDao instance = new InMemoryRocketsDao();

    private InMemoryRocketsDao() {
        store = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(RocketEntity rocket) {
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
