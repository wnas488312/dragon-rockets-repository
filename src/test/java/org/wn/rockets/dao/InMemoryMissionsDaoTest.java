package org.wn.rockets.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.wn.rockets.entity.MissionEntity;
import org.wn.rockets.entity.MissionStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryMissionsDaoTest {
    private final InMemoryMissionsDao missionsDao = InMemoryMissionsDao.getInstance();

    @AfterEach
    public void cleanUp() {
        missionsDao.removeAll();
    }

    @Test
    void getInstance_ensureSameInstanceTest() {
        final InMemoryMissionsDao instance1 = InMemoryMissionsDao.getInstance();
        final InMemoryMissionsDao instance2 = InMemoryMissionsDao.getInstance();

        assertEquals(instance1, instance2);
    }

    @Test
    void findTest() {
        final MissionEntity missionEntity = new MissionEntity("Mars", MissionStatus.SCHEDULED);
        missionsDao.save(missionEntity);

        final Optional<MissionEntity> found = missionsDao.find(missionEntity.missionName());
        assertTrue(found.isPresent());
    }

    @Test
    void find_emptyTest() {
        final Optional<MissionEntity> found = missionsDao.find("Not found");
        assertTrue(found.isEmpty());
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
        assertTrue(allMissions.contains(missionEntity1));
        assertTrue(allMissions.contains(missionEntity2));
    }

    @Test
    void removeAllTest() {
        final MissionEntity missionEntity1 = new MissionEntity("Mars", MissionStatus.SCHEDULED);
        missionsDao.save(missionEntity1);

        final MissionEntity missionEntity2 = new MissionEntity("Luna", MissionStatus.PENDING);
        missionsDao.save(missionEntity2);

        missionsDao.removeAll();

        List<MissionEntity> allMissions = missionsDao.getAll();
        assertTrue(allMissions.isEmpty());
    }

    @Test
    void exists_existsTest() {
        final MissionEntity missionEntity = new MissionEntity("Mars", MissionStatus.SCHEDULED);
        missionsDao.save(missionEntity);

        assertTrue(missionsDao.exists(missionEntity.missionName()));
    }

    @Test
    void exists_notExistsTest() {
        assertFalse(missionsDao.exists("Not exists"));
    }
}