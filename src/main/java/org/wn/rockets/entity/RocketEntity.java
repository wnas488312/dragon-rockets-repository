package org.wn.rockets.entity;

/**
 * Object that stores data about rocket.
 * @param rocketName            Name of a rocket, will be used as identifier.
 * @param status                Current status of a rocket.
 * @param assignedMissionName   Name of a mission (also ID) that rocket is assigned to.
 */
public record RocketEntity(
        String rocketName,
        RocketStatus status,
        String assignedMissionName
) {
}
