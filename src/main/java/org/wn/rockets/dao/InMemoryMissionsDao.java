package org.wn.rockets.dao;

import lombok.Getter;
import org.wn.rockets.entity.MissionEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMissionsDao implements MissionsDao {
    private final Map<String, MissionEntity> store;

    @Getter
    private static final InMemoryMissionsDao instance = new InMemoryMissionsDao();

    private InMemoryMissionsDao() {
        store = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(MissionEntity entity) {
        // TODO: actual implementation.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MissionEntity> getAll() {
        // TODO: actual implementation.
        return List.of();
    }
}
