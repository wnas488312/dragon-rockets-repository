package org.wn.rockets;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.mockito.Mockito;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class RocketsRepositoryImplTest {
    private static final List<String> EXAMPLE_ROCKETS_NAMES_LIST = List.of("Dragon 1", "Dragon 2", "Dragon 3");
    private static final String EXAMPLE_ROCKET_NAME = EXAMPLE_ROCKETS_NAMES_LIST.getFirst();
    private static final String EXAMPLE_MISSION_NAME = "Mars";

    private final RocketsDao rocketsDao = Mockito.mock(RocketsDao.class);
    private final MissionsDao missionsDao = Mockito.mock(MissionsDao.class);

    private final RocketsRepository repository = new RocketsRepositoryImpl(rocketsDao, missionsDao);

    @Test
    void addNewRocket_successTest() {
        Mockito.when(rocketsDao.exists(eq(EXAMPLE_ROCKET_NAME))).thenReturn(false);

        ArgumentCaptor<RocketEntity> entityArgumentCaptor = ArgumentCaptor.forClass(RocketEntity.class);
        Mockito.doNothing().when(rocketsDao).save(entityArgumentCaptor.capture());

        repository.addNewRocket(EXAMPLE_ROCKET_NAME);

        Mockito.verify(rocketsDao, Mockito.times(1)).save(any(RocketEntity.class));

        final RocketEntity captured = entityArgumentCaptor.getValue();
        assertEquals(EXAMPLE_ROCKET_NAME, captured.rocketName());
        assertEquals(RocketStatus.ON_GROUND, captured.status());
        assertNull(captured.assignedMissionName());
    }

    @Test
    void addNewRocket_alreadyExists_expectErrorTest() {
        Mockito.when(rocketsDao.exists(eq(EXAMPLE_ROCKET_NAME))).thenReturn(true);

        assertThrows(AlreadyPresentInStoreException.class, () -> repository.addNewRocket(EXAMPLE_ROCKET_NAME));
    }

    @Test
    void assignRocketToMission_successTest() {
        Mockito.when(rocketsDao.find(eq(EXAMPLE_ROCKET_NAME))).thenReturn(
                Optional.of(new RocketEntity(EXAMPLE_ROCKET_NAME, RocketStatus.ON_GROUND, null)));
        Mockito.when(missionsDao.exists(eq(EXAMPLE_MISSION_NAME))).thenReturn(true);

        ArgumentCaptor<RocketEntity> entityArgumentCaptor = ArgumentCaptor.forClass(RocketEntity.class);
        Mockito.doNothing().when(rocketsDao).save(entityArgumentCaptor.capture());

        repository.assignRocketToMission(EXAMPLE_ROCKET_NAME, EXAMPLE_MISSION_NAME);

        final RocketEntity captured = entityArgumentCaptor.getValue();
        assertEquals(EXAMPLE_ROCKET_NAME, captured.rocketName());
        assertEquals(RocketStatus.ON_GROUND, captured.status());
        assertEquals(EXAMPLE_MISSION_NAME, captured.assignedMissionName());
    }

    @Test
    void assignRocketToMission_rocketNotFound_expectErrorTest() {
        Mockito.when(rocketsDao.find(eq(EXAMPLE_ROCKET_NAME))).thenReturn(Optional.empty());
        Mockito.when(missionsDao.exists(eq(EXAMPLE_MISSION_NAME))).thenReturn(true);

        assertThrows(NotFoundException.class,
                () -> repository.assignRocketToMission(EXAMPLE_ROCKET_NAME, EXAMPLE_MISSION_NAME)
        );
    }

    @Test
    void assignRocketToMission_missionNotFound_expectErrorTest() {
        Mockito.when(missionsDao.exists(eq(EXAMPLE_MISSION_NAME))).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> repository.assignRocketToMission(EXAMPLE_ROCKET_NAME, EXAMPLE_MISSION_NAME)
        );
    }

    @Test
    void assignRocketsToMission_successTest() {
        Mockito.when(rocketsDao.find(anyString())).thenReturn(
                Optional.of(new RocketEntity("rocketName", RocketStatus.ON_GROUND, null)));
        Mockito.when(missionsDao.exists(eq(EXAMPLE_MISSION_NAME))).thenReturn(true);

        ArgumentCaptor<RocketEntity> entityArgumentCaptor = ArgumentCaptor.forClass(RocketEntity.class);
        Mockito.doNothing().when(rocketsDao).save(entityArgumentCaptor.capture());

        repository.assignRocketsToMission(EXAMPLE_ROCKETS_NAMES_LIST, EXAMPLE_MISSION_NAME);

        final List<RocketEntity> rocketEntities = entityArgumentCaptor.getAllValues();
        assertEquals(3, rocketEntities.size());
        assertEquals(EXAMPLE_MISSION_NAME, rocketEntities.get(0).assignedMissionName());
        assertEquals(EXAMPLE_MISSION_NAME, rocketEntities.get(1).assignedMissionName());
        assertEquals(EXAMPLE_MISSION_NAME, rocketEntities.get(2).assignedMissionName());
    }

    @Test
    void assignRocketsToMission_rocketNotFound_expectErrorTest() {
        Mockito.when(rocketsDao.find(anyString())).thenReturn(Optional.empty());
        Mockito.when(missionsDao.exists(eq(EXAMPLE_MISSION_NAME))).thenReturn(true);

        assertThrows(NotFoundException.class,
                () -> repository.assignRocketsToMission(EXAMPLE_ROCKETS_NAMES_LIST, EXAMPLE_MISSION_NAME)
        );
    }

    @Test
    void assignRocketsToMission_missionNotFound_expectErrorTest() {
        Mockito.when(missionsDao.exists(eq(EXAMPLE_MISSION_NAME))).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> repository.assignRocketsToMission(EXAMPLE_ROCKETS_NAMES_LIST, EXAMPLE_MISSION_NAME)
        );
    }

    @Test
    void changeRocketStatus_successTest() {
        final RocketStatus status = RocketStatus.IN_SPACE;

        Mockito.when(rocketsDao.find(eq(EXAMPLE_ROCKET_NAME))).thenReturn(
                Optional.of(new RocketEntity(EXAMPLE_ROCKET_NAME, RocketStatus.ON_GROUND, EXAMPLE_MISSION_NAME)));

        ArgumentCaptor<RocketEntity> entityArgumentCaptor = ArgumentCaptor.forClass(RocketEntity.class);
        Mockito.doNothing().when(rocketsDao).save(entityArgumentCaptor.capture());

        repository.changeRocketStatus(EXAMPLE_ROCKET_NAME, status);

        final RocketEntity captured = entityArgumentCaptor.getValue();
        assertEquals(status, captured.status());
    }

    @Test
    void changeRocketStatus_toInRepair_expectMissionStatusChange_successTest() {
        final RocketStatus status = RocketStatus.IN_REPAIR;

        Mockito.when(rocketsDao.find(eq(EXAMPLE_ROCKET_NAME))).thenReturn(
                Optional.of(new RocketEntity(EXAMPLE_ROCKET_NAME, RocketStatus.IN_SPACE, EXAMPLE_MISSION_NAME)));

        Mockito.when(missionsDao.find(eq(EXAMPLE_MISSION_NAME))).thenReturn(
                Optional.of(new MissionEntity(EXAMPLE_MISSION_NAME, MissionStatus.IN_PROGRESS)));

        ArgumentCaptor<RocketEntity> rocketEntityArgumentCaptor = ArgumentCaptor.forClass(RocketEntity.class);
        Mockito.doNothing().when(rocketsDao).save(rocketEntityArgumentCaptor.capture());

        ArgumentCaptor<MissionEntity> missionEntityArgumentCaptor = ArgumentCaptor.forClass(MissionEntity.class);
        Mockito.doNothing().when(missionsDao).save(missionEntityArgumentCaptor.capture());

        repository.changeRocketStatus(EXAMPLE_ROCKET_NAME, status);

        final RocketEntity capturedRocket = rocketEntityArgumentCaptor.getValue();
        assertEquals(status, capturedRocket.status());

        final MissionEntity capturedMission = missionEntityArgumentCaptor.getValue();
        assertEquals(MissionStatus.PENDING, capturedMission.status());
    }

    @Test
    void changeRocketStatus_withoutMission_expectError() {
        final RocketStatus status = RocketStatus.IN_SPACE;

        Mockito.when(rocketsDao.find(eq(EXAMPLE_ROCKET_NAME))).thenReturn(
                Optional.of(new RocketEntity(EXAMPLE_ROCKET_NAME, RocketStatus.ON_GROUND, null)));

        assertThrows(WrongStatusException.class, () -> repository.changeRocketStatus(EXAMPLE_ROCKET_NAME, status));
    }

    @Test
    void changeRocketStatus_notFound_expectErrorTest() {
        final RocketStatus status = RocketStatus.IN_SPACE;

        Mockito.when(rocketsDao.find(eq(EXAMPLE_ROCKET_NAME))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> repository.changeRocketStatus(EXAMPLE_ROCKET_NAME, status));
    }
}