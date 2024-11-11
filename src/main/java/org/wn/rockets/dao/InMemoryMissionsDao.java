package org.wn.rockets.dao;

import lombok.Getter;
import org.wn.rockets.entity.MissionEntity;
import org.wn.rockets.entity.MissionStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMissionsDao implements MissionsDao {
    private Map<String, MissionEntity> store = new HashMap<>();

    @Getter
    private static final InMemoryMissionsDao instance = new InMemoryMissionsDao();

    private InMemoryMissionsDao() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNew(MissionEntity entity) {
        // TODO: actual implementation.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeStatus(String identifier, MissionStatus status) {
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
