package org.wn.rockets;

import org.wn.rockets.dao.InMemoryMissionsDao;
import org.wn.rockets.dao.InMemoryRocketsDao;
import org.wn.rockets.dao.MissionsDao;
import org.wn.rockets.dao.RocketsDao;
import org.wn.rockets.dto.MissionSummaryDto;
import org.wn.rockets.dto.RocketSummaryDto;
import org.wn.rockets.entity.MissionEntity;
import org.wn.rockets.entity.MissionStatus;
import org.wn.rockets.entity.RocketEntity;
import org.wn.rockets.entity.RocketStatus;
import org.wn.rockets.exception.AlreadyPresentInStoreException;
import org.wn.rockets.exception.NotFoundException;
import org.wn.rockets.exception.WrongStatusException;

import java.util.ArrayList;
import java.util.Comparator;
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
        if (missionsDao.exists(missionName)) {
            throw new AlreadyPresentInStoreException(String.format("Mission with name %s is already present in repository.", missionName));
        }

        final MissionEntity newMissionEntity = new MissionEntity(
                missionName,
                MissionStatus.SCHEDULED
        );

        missionsDao.save(newMissionEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeMissionStatus(String missionName, MissionStatus status) throws NotFoundException, WrongStatusException {
        final MissionEntity missionEntity = getMissionEntityIfPresent(missionName);

        final List<RocketEntity> rocketsByMissionName = rocketsDao.getRocketsByMissionName(missionName);
        if (MissionStatus.PENDING.equals(status) &&
                (rocketsByMissionName.isEmpty() ||
                        rocketsByMissionName.stream().noneMatch(rocket -> RocketStatus.IN_REPAIR.equals(rocket.status())))) {
            throw new WrongStatusException(
                    String.format("Cannot change status to PENDING if there is no rockets assigned to mission %s", missionName));
        }

        if (MissionStatus.IN_PROGRESS.equals(status) &&
                (rocketsByMissionName.isEmpty() ||
                        rocketsByMissionName.stream().anyMatch(rocket -> RocketStatus.IN_REPAIR.equals(rocket.status())))) {
            throw new WrongStatusException(
                    String.format("Cannot change status to IN_PROGRESS if there is no rockets assigned to mission %s or any of them is in repair", missionName));
        }

        if (MissionStatus.ENDED.equals(status)) {
            for (RocketEntity rocket: rocketsByMissionName) {
                final RocketEntity updatedRocket = rocket.withAssignedMissionName(null);
                rocketsDao.save(updatedRocket);
            }
        }

        final MissionEntity updatedMissionEntity = missionEntity.withStatus(status);
        missionsDao.save(updatedMissionEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MissionSummaryDto> getMissionsSummary() {
        final List<MissionEntity> everyMissionPresent = missionsDao.getAll();
        final List<MissionSummaryDto> result = new ArrayList<>(everyMissionPresent.size());
        for (MissionEntity missionEntity: everyMissionPresent) {
            final List<RocketSummaryDto> rocketsSummary  =
                    rocketsDao.getRocketsByMissionName(missionEntity.missionName())
                    .stream().map(rocket -> new RocketSummaryDto(rocket.rocketName(), rocket.status()))
                    .toList();
            final MissionSummaryDto missionSummary = new MissionSummaryDto(
                    missionEntity.missionName(),
                    missionEntity.status(),
                    rocketsSummary
            );
            result.add(missionSummary);
        }

        result.sort(Comparator
                .comparingInt((MissionSummaryDto missionSummary) -> missionSummary.dragons().size())
                .thenComparing(MissionSummaryDto::missionName)
                .reversed());

        return result;
    }

    private MissionEntity getMissionEntityIfPresent(String missionName) {
        return missionsDao.find(missionName)
                .orElseThrow(() -> new NotFoundException(String.format("Mission with name %s is not present in repository.", missionName)));
    }
}
