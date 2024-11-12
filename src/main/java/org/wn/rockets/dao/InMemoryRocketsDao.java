package org.wn.rockets.dao;

import lombok.Getter;
import org.wn.rockets.entity.RocketEntity;

import java.util.*;

public class InMemoryRocketsDao implements RocketsDao {
    private Map<String, RocketEntity> store;

    @Getter
    private static final InMemoryRocketsDao instance = new InMemoryRocketsDao();

    private InMemoryRocketsDao() {
        store = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RocketEntity> find(String identifier) {
        return Optional.ofNullable(store.get(identifier));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(RocketEntity rocket) {
        final String key = rocket.rocketName();
        store.put(key, rocket);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RocketEntity> getRocketsByMissionName(String missionName) {
        return store.values()
                .stream()
                .filter(rocket -> Objects.equals(missionName, rocket.assignedMissionName()))
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll() {
        store = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(String identifier) {
        return store.containsKey(identifier);
    }
}
