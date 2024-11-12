package org.wn.rockets;

import org.wn.rockets.dao.InMemoryMissionsDao;
import org.wn.rockets.dao.InMemoryRocketsDao;
import org.wn.rockets.dao.MissionsDao;
import org.wn.rockets.dao.RocketsDao;
import org.wn.rockets.entity.RocketStatus;
import org.wn.rockets.exception.AlreadyPresentInStoreException;
import org.wn.rockets.exception.NotFoundException;

import java.util.List;

public class RocketsRepositoryImpl implements RocketsRepository {
    private final RocketsDao rocketsDao;
    private final MissionsDao missionsDao;

    public RocketsRepositoryImpl() {
        rocketsDao = InMemoryRocketsDao.getInstance();
        missionsDao = InMemoryMissionsDao.getInstance();
    }

    /**
     * Constructor used when there is a need to override dependencies
     * @param rocketsDao    DAO object for rockets
     * @param missionsDao   DAO object for missions
     */
    public RocketsRepositoryImpl(RocketsDao rocketsDao, MissionsDao missionsDao) {
        this.rocketsDao = rocketsDao;
        this.missionsDao = missionsDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNewRocket(String rocketName) throws AlreadyPresentInStoreException {
        // TODO: Implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignRocketToMission(String rocketName, String missionName) throws NotFoundException {
        // TODO: Implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignRocketsToMission(List<String> rocketsNames, String missionName) throws NotFoundException {
        // TODO: Implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeRocketStatus(String rocketName, RocketStatus status) throws NotFoundException {
        // TODO: Implementation
    }
}
