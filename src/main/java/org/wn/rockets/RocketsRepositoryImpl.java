package org.wn.rockets;

import org.wn.rockets.dao.InMemoryMissionsDao;
import org.wn.rockets.dao.InMemoryRocketsDao;
import org.wn.rockets.dao.MissionsDao;
import org.wn.rockets.dao.RocketsDao;
import org.wn.rockets.entity.MissionEntity;
import org.wn.rockets.entity.MissionStatus;
import org.wn.rockets.entity.RocketEntity;
import org.wn.rockets.entity.RocketStatus;
import org.wn.rockets.exception.AlreadyPresentInStoreException;
import org.wn.rockets.exception.NotFoundException;
import org.wn.rockets.exception.WrongStatusException;

import java.util.List;
import java.util.Objects;

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
        if (rocketsDao.exists(rocketName)) {
            throw new AlreadyPresentInStoreException(String.format("Rocket with name %s is already present in repository.", rocketName));
        }

        final RocketEntity newEntity = new RocketEntity(
                rocketName,
                RocketStatus.ON_GROUND,
                null
        );

        rocketsDao.save(newEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignRocketToMission(String rocketName, String missionName) throws NotFoundException {
        if (!missionsDao.exists(missionName)) {
            throw new NotFoundException(String.format("Mission with name %s is not present in repository.", missionName));
        }

        final RocketEntity rocketEntity = getRocketEntityIfPresent(rocketName);

        final RocketEntity updatedRocketEntity = rocketEntity.withAssignedMissionName(missionName);
        rocketsDao.save(updatedRocketEntity);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignRocketsToMission(List<String> rocketsNames, String missionName) throws NotFoundException {
        for (String rocketName: rocketsNames) {
            assignRocketToMission(rocketName, missionName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeRocketStatus(String rocketName, RocketStatus status) throws NotFoundException, WrongStatusException {
        final RocketEntity rocketEntity = getRocketEntityIfPresent(rocketName);

        final String assignedMissionName = rocketEntity.assignedMissionName();

        if (!RocketStatus.ON_GROUND.equals(status) && Objects.isNull(assignedMissionName)) {
            throw new WrongStatusException(String.format("Rocket with name %s is not assigned to any mission", rocketName));
        }

        if (RocketStatus.IN_REPAIR.equals(status)) {
            final MissionEntity missionEntity = missionsDao.find(assignedMissionName)
                    .orElseThrow(() -> new NotFoundException(String.format("Mission with name %s is not present in repository.", assignedMissionName)));
            if (MissionStatus.IN_PROGRESS.equals(missionEntity.status())) {
                final MissionEntity updatedMissionEntity = missionEntity.withStatus(MissionStatus.PENDING);
                missionsDao.save(updatedMissionEntity);
            }
        }

        final RocketEntity updatedRocketEntity = rocketEntity.withStatus(status);
        rocketsDao.save(updatedRocketEntity);
    }

    private RocketEntity getRocketEntityIfPresent(String rocketName) {
        return rocketsDao.find(rocketName)
                .orElseThrow(() -> new NotFoundException(String.format("Rocket with name %s is not present in repository.", rocketName)));
    }
}
