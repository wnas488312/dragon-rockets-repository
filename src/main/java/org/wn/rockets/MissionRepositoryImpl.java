package org.wn.rockets;

import org.wn.rockets.dao.InMemoryMissionsDao;
import org.wn.rockets.dao.InMemoryRocketsDao;
import org.wn.rockets.dao.MissionsDao;
import org.wn.rockets.dao.RocketsDao;
import org.wn.rockets.dto.MissionSummaryDto;
import org.wn.rockets.entity.MissionStatus;
import org.wn.rockets.exception.AlreadyPresentInStoreException;
import org.wn.rockets.exception.NotFoundException;

import java.util.List;

public class MissionRepositoryImpl implements MissionRepository {
    private final RocketsDao rocketsDao;
    private final MissionsDao missinsDao;

    public MissionRepositoryImpl() {
        rocketsDao = InMemoryRocketsDao.getInstance();
        missinsDao = InMemoryMissionsDao.getInstance();
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
    public void changeMissionStatus(String missionName, MissionStatus status) throws NotFoundException {
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
