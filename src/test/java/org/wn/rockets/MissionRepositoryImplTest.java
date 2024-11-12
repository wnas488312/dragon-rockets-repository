package org.wn.rockets;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class MissionRepositoryImplTest {
    private static final String EXAMPLE_MISSION_NAME = "Mars";
    private static final MissionStatus EXAMPLE_MISSION_STATUS = MissionStatus.PENDING;

    private final RocketsDao rocketsDao = Mockito.mock(RocketsDao.class);
    private final MissionsDao missionsDao = Mockito.mock(MissionsDao.class);

    private final MissionRepository repository = new MissionRepositoryImpl(rocketsDao, missionsDao);

    @Test
    void addNewMission_successTest() {
        Mockito.when(missionsDao.exists(eq(EXAMPLE_MISSION_NAME))).thenReturn(false);

        ArgumentCaptor<MissionEntity> entityArgumentCaptor = ArgumentCaptor.forClass(MissionEntity.class);
        Mockito.doNothing().when(missionsDao).save(entityArgumentCaptor.capture());

        repository.addNewMission(EXAMPLE_MISSION_NAME);

        Mockito.verify(missionsDao, Mockito.times(1)).save(any(MissionEntity.class));

        final MissionEntity captured = entityArgumentCaptor.getValue();
        assertEquals(EXAMPLE_MISSION_NAME, captured.missionName());
        assertEquals(MissionStatus.SCHEDULED, captured.status());
    }

    @Test
    void addNewMission_alreadyExists_expectErrorTest() {
        Mockito.when(missionsDao.exists(eq(EXAMPLE_MISSION_NAME))).thenReturn(true);

        assertThrows(AlreadyPresentInStoreException.class, () -> repository.addNewMission(EXAMPLE_MISSION_NAME));
    }

    @Test
    void changeMissionStatus_successTest() {
        Mockito.when(missionsDao.find(eq(EXAMPLE_MISSION_NAME)))
                .thenReturn(Optional.of(new MissionEntity(EXAMPLE_MISSION_NAME, MissionStatus.SCHEDULED)));

        Mockito.when(rocketsDao.getRocketsByMissionName(EXAMPLE_MISSION_NAME))
                .thenReturn(List.of(new RocketEntity("Dragon 1", RocketStatus.ON_GROUND, EXAMPLE_MISSION_NAME)));

        ArgumentCaptor<MissionEntity> entityArgumentCaptor = ArgumentCaptor.forClass(MissionEntity.class);
        Mockito.doNothing().when(missionsDao).save(entityArgumentCaptor.capture());

        repository.changeMissionStatus(EXAMPLE_MISSION_NAME, EXAMPLE_MISSION_STATUS);

        Mockito.verify(missionsDao, Mockito.times(1)).save(any(MissionEntity.class));

        final MissionEntity captured = entityArgumentCaptor.getValue();
        assertEquals(EXAMPLE_MISSION_NAME, captured.missionName());
        assertEquals(EXAMPLE_MISSION_STATUS, captured.status());
    }

    @Test
    void changeMissionStatus_ended_ensureRocketsAreUnassigned_successTest() {
        Mockito.when(missionsDao.find(eq(EXAMPLE_MISSION_NAME)))
                .thenReturn(Optional.of(new MissionEntity(EXAMPLE_MISSION_NAME, MissionStatus.SCHEDULED)));

        Mockito.when(rocketsDao.getRocketsByMissionName(EXAMPLE_MISSION_NAME))
                .thenReturn(List.of(new RocketEntity("Dragon 1", RocketStatus.ON_GROUND, EXAMPLE_MISSION_NAME)));

        ArgumentCaptor<MissionEntity> missionEntityArgumentCaptor = ArgumentCaptor.forClass(MissionEntity.class);
        Mockito.doNothing().when(missionsDao).save(missionEntityArgumentCaptor.capture());

        ArgumentCaptor<RocketEntity> rocketEntityArgumentCaptor = ArgumentCaptor.forClass(RocketEntity.class);
        Mockito.doNothing().when(rocketsDao).save(rocketEntityArgumentCaptor.capture());

        repository.changeMissionStatus(EXAMPLE_MISSION_NAME, MissionStatus.ENDED);

        Mockito.verify(missionsDao, Mockito.times(1)).save(any(MissionEntity.class));

        final MissionEntity missionCaptured = missionEntityArgumentCaptor.getValue();
        assertEquals(EXAMPLE_MISSION_NAME, missionCaptured.missionName());
        assertEquals(MissionStatus.ENDED, missionCaptured.status());

        final RocketEntity rocketCaptured = rocketEntityArgumentCaptor.getValue();
        assertNull(rocketCaptured.assignedMissionName());
    }

    @Test
    void changeMissionStatus_pendingWithoutRockets_expectErrorTest() {
        Mockito.when(missionsDao.find(eq(EXAMPLE_MISSION_NAME)))
                .thenReturn(Optional.of(new MissionEntity(EXAMPLE_MISSION_NAME, MissionStatus.SCHEDULED)));

        Mockito.when(rocketsDao.getRocketsByMissionName(EXAMPLE_MISSION_NAME)).thenReturn(Collections.emptyList());

        assertThrows(WrongStatusException.class, () -> repository.changeMissionStatus(EXAMPLE_MISSION_NAME, MissionStatus.PENDING));
    }

    @Test
    void changeMissionStatus_inProgressWithoutRockets_expectErrorTest() {
        Mockito.when(missionsDao.find(eq(EXAMPLE_MISSION_NAME)))
                .thenReturn(Optional.of(new MissionEntity(EXAMPLE_MISSION_NAME, MissionStatus.SCHEDULED)));

        Mockito.when(rocketsDao.getRocketsByMissionName(EXAMPLE_MISSION_NAME)).thenReturn(Collections.emptyList());

        assertThrows(WrongStatusException.class, () -> repository.changeMissionStatus(EXAMPLE_MISSION_NAME, MissionStatus.IN_PROGRESS));
    }

    @Test
    void changeMissionStatus_inProgressWithRocketInRepair_expectErrorTest() {
        Mockito.when(missionsDao.find(eq(EXAMPLE_MISSION_NAME)))
                .thenReturn(Optional.of(new MissionEntity(EXAMPLE_MISSION_NAME, MissionStatus.SCHEDULED)));

        Mockito.when(rocketsDao.getRocketsByMissionName(EXAMPLE_MISSION_NAME))
                .thenReturn(List.of(new RocketEntity("Dragon 1", RocketStatus.IN_REPAIR, EXAMPLE_MISSION_NAME)));

        assertThrows(WrongStatusException.class, () -> repository.changeMissionStatus(EXAMPLE_MISSION_NAME, MissionStatus.IN_PROGRESS));
    }

    @Test
    void changeMissionStatus_notFound_expectErrorTest() {
        Mockito.when(missionsDao.find(eq(EXAMPLE_MISSION_NAME))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> repository.changeMissionStatus(EXAMPLE_MISSION_NAME, EXAMPLE_MISSION_STATUS));
    }

    @Test
    void getMissionsSummaryTest() {
        Mockito.when(missionsDao.getAll()).thenReturn(prepareMissionEntitiesInRandomOrder());
        Mockito.when(rocketsDao.getRocketsByMissionName(anyString())).thenReturn(Collections.emptyList());
        Mockito.when(rocketsDao.getRocketsByMissionName(eq("Luna1"))).thenReturn(prepareLuna1Rockets());
        Mockito.when(rocketsDao.getRocketsByMissionName(eq("Transit"))).thenReturn(prepareTransitRockets());

        final List<MissionSummaryDto> expectedResult = prepareExpectedResult();

        final List<MissionSummaryDto> missionsSummary = repository.getMissionsSummary();
        assertEquals(expectedResult, missionsSummary);
    }

    private static List<MissionSummaryDto> prepareExpectedResult() {
        return List.of(
                new MissionSummaryDto("Transit", MissionStatus.IN_PROGRESS, List.of(
                        new RocketSummaryDto("Red Dragon", RocketStatus.ON_GROUND),
                        new RocketSummaryDto("Dragon XL", RocketStatus.IN_SPACE),
                        new RocketSummaryDto("Falcon Heavy", RocketStatus.IN_SPACE)
                )),
                new MissionSummaryDto("Luna1", MissionStatus.PENDING, List.of(
                        new RocketSummaryDto("Dragon 1", RocketStatus.ON_GROUND),
                        new RocketSummaryDto("Dragon 2", RocketStatus.ON_GROUND)
                )),
                new MissionSummaryDto("Vertical Landing", MissionStatus.ENDED, Collections.emptyList()),
                new MissionSummaryDto("Mars", MissionStatus.SCHEDULED, Collections.emptyList()),
                new MissionSummaryDto("Luna2", MissionStatus.SCHEDULED, Collections.emptyList()),
                new MissionSummaryDto("Double Landing", MissionStatus.ENDED, Collections.emptyList())
        );
    }

    private static List<MissionEntity> prepareMissionEntitiesInRandomOrder() {
        return List.of(
                new MissionEntity("Mars", MissionStatus.SCHEDULED),
                new MissionEntity("Luna1", MissionStatus.PENDING),
                new MissionEntity("Double Landing", MissionStatus.ENDED),
                new MissionEntity("Transit", MissionStatus.IN_PROGRESS),
                new MissionEntity("Luna2", MissionStatus.SCHEDULED),
                new MissionEntity("Vertical Landing", MissionStatus.ENDED)
        );
    }

    private static List<RocketEntity> prepareLuna1Rockets() {
        return List.of(
                new RocketEntity("Dragon 1", RocketStatus.ON_GROUND, "Luna1"),
                new RocketEntity("Dragon 2", RocketStatus.ON_GROUND, "Luna1")
        );
    }

    private static List<RocketEntity> prepareTransitRockets() {
        return List.of(
                new RocketEntity("Red Dragon", RocketStatus.ON_GROUND, "Transit"),
                new RocketEntity("Dragon XL", RocketStatus.IN_SPACE, "Transit"),
                new RocketEntity("Falcon Heavy", RocketStatus.IN_SPACE, "Transit")
        );
    }
}