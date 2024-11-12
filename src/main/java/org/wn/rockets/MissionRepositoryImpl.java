package org.wn.rockets;

import org.wn.rockets.dao.InMemoryMissionsDao;
import org.wn.rockets.dao.InMemoryRocketsDao;
import org.wn.rockets.dao.MissionsDao;
import org.wn.rockets.dao.RocketsDao;
import org.wn.rockets.dto.MissionSummaryDto;
import org.wn.rockets.entity.MissionStatus;
import org.wn.rockets.exception.AlreadyPresentInStoreException;
import org.wn.rockets.exception.NotFoundException;
import org.wn.rockets.exception.WrongStatusException;

import java.util.List;

public class MissionRepositoryImpl implements MissionRepository {
    private final RocketsDao rocketsDao;
    private final MissionsDao missionsDao;

    public MissionRepositoryImpl() {
        rocketsDao = InMemoryRocketsDao.getInstance();
        missionsDao = InMemoryMissionsDao.getInstance();
    }

    public MissionRepositoryImpl(RocketsDao rocketsDao, MissionsDao missionsDao) {
        this.rocketsDao = rocketsDao;
        this.missionsDao = missionsDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewMission(String missionName) throws AlreadyPresentInStoreException {
        // TODO: Implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeMissionStatus(String missionName, MissionStatus status) throws NotFoundException, WrongStatusException {
        // TODO: Implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MissionSummaryDto> getMissionsSummary() {
        // TODO: Implementation
        return List.of();
    }
}
