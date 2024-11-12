package org.wn.rockets.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.wn.rockets.entity.RocketEntity;
import org.wn.rockets.entity.RocketStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRocketsDaoTest {
    private final InMemoryRocketsDao rocketsDao = InMemoryRocketsDao.getInstance();

    @AfterEach
    public void cleanUp() {
        rocketsDao.removeAll();
    }

    @Test
    void getInstance_ensureSameInstanceTest() {
        final InMemoryRocketsDao instance1 = InMemoryRocketsDao.getInstance();
        final InMemoryRocketsDao instance2 = InMemoryRocketsDao.getInstance();

        assertEquals(instance1, instance2);
    }

    @Test
    void save_addNewTest() {
        final RocketEntity rocketEntity = new RocketEntity("Dragon 1", RocketStatus.ON_GROUND, "Mars");
        rocketsDao.save(rocketEntity);

        List<RocketEntity> rocketsByMissionName = rocketsDao.getRocketsByMissionName(rocketEntity.assignedMissionName());
        assertEquals(1, rocketsByMissionName.size());
        assertEquals(rocketEntity, rocketsByMissionName.getFirst());
    }

    @Test
    void save_alreadyExistsTest() {
        final RocketEntity rocketEntity1 = new RocketEntity("Dragon 1", RocketStatus.ON_GROUND, "Mars");
        rocketsDao.save(rocketEntity1);

        final RocketEntity rocketEntity2 = new RocketEntity("Dragon 1", RocketStatus.IN_SPACE, "Luna");
        rocketsDao.save(rocketEntity2);

        List<RocketEntity> rocketsByMissionName = rocketsDao.getRocketsByMissionName(rocketEntity2.assignedMissionName());
        assertEquals(1, rocketsByMissionName.size());
        assertEquals(rocketEntity2, rocketsByMissionName.getFirst());
    }

    @Test
    void getRocketsByMissionName() {
        final String missionName1 = "Mars";
        final String missionName2 = "Luna";

        final RocketEntity rocketEntity1 = new RocketEntity("Dragon 1", RocketStatus.ON_GROUND, missionName1);
        rocketsDao.save(rocketEntity1);

        final RocketEntity rocketEntity2 = new RocketEntity("Dragon 2", RocketStatus.ON_GROUND, missionName1);
        rocketsDao.save(rocketEntity2);

        final RocketEntity rocketEntity3 = new RocketEntity("Dragon 3", RocketStatus.ON_GROUND, missionName2);
        rocketsDao.save(rocketEntity3);

        List<RocketEntity> rocketsByMissionName = rocketsDao.getRocketsByMissionName(missionName1);
        assertEquals(2, rocketsByMissionName.size());
        assertTrue(rocketsByMissionName.contains(rocketEntity1));
        assertTrue(rocketsByMissionName.contains(rocketEntity2));
    }

    @Test
    void removeAllTest() {
        final String missionName = "Mars";

        final RocketEntity rocketEntity1 = new RocketEntity("Dragon 1", RocketStatus.ON_GROUND, missionName);
        rocketsDao.save(rocketEntity1);

        final RocketEntity rocketEntity2 = new RocketEntity("Dragon 2", RocketStatus.ON_GROUND, missionName);
        rocketsDao.save(rocketEntity2);

        rocketsDao.removeAll();

        List<RocketEntity> rocketsByMissionName = rocketsDao.getRocketsByMissionName(missionName);
        assertTrue(rocketsByMissionName.isEmpty());
    }
}