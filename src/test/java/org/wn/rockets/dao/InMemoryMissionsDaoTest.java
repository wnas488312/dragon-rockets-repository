package org.wn.rockets.dao;

import org.junit.jupiter.api.Test;
import org.wn.rockets.entity.MissionEntity;
import org.wn.rockets.entity.MissionStatus;
import org.wn.rockets.entity.RocketEntity;
import org.wn.rockets.entity.RocketStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryMissionsDaoTest {
    private final InMemoryMissionsDao missionsDao = InMemoryMissionsDao.getInstance();

    @Test
    void getInstance_ensureSameInstanceTest() {
        final InMemoryMissionsDao instance1 = InMemoryMissionsDao.getInstance();
        final InMemoryMissionsDao instance2 = InMemoryMissionsDao.getInstance();

        assertEquals(instance1, instance2);
    }

    @Test
    void save_addNewTest() {
        final MissionEntity missionEntity = new MissionEntity("Mars", MissionStatus.SCHEDULED);
        missionsDao.save(missionEntity);

        List<MissionEntity> allMissions = missionsDao.getAll();
        assertEquals(1, allMissions.size());
        assertEquals(missionEntity, allMissions.getFirst());
    }

    @Test
    void save_alreadyExistsTest() {
        final MissionEntity missionEntity1 = new MissionEntity("Mars", MissionStatus.SCHEDULED);
        missionsDao.save(missionEntity1);

        final MissionEntity missionEntity2 = new MissionEntity("Mars", MissionStatus.PENDING);
        missionsDao.save(missionEntity2);

        List<MissionEntity> allMissions = missionsDao.getAll();
        assertEquals(1, allMissions.size());
        assertEquals(missionEntity2, allMissions.getFirst());
    }

    @Test
    void getAllTest() {
        final MissionEntity missionEntity1 = new MissionEntity("Mars", MissionStatus.SCHEDULED);
        missionsDao.save(missionEntity1);

        final MissionEntity missionEntity2 = new MissionEntity("Luna", MissionStatus.PENDING);
        missionsDao.save(missionEntity2);

        List<MissionEntity> allMissions = missionsDao.getAll();
        assertEquals(2, allMissions.size());
        assertEquals(missionEntity1, allMissions.getFirst());
        assertEquals(missionEntity2, allMissions.getLast());
    }
}