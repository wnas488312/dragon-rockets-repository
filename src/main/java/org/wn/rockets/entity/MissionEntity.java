package org.wn.rockets.entity;

/**
 * Object that stores data about mission.
 * @param missionName   Name of a mission that will be used also as its identifier.
 * @param status        Current status of a mission.
 */
public record MissionEntity(
        String missionName,
        MissionStatus status
) {
}
