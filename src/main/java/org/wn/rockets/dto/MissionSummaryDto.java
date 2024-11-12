package org.wn.rockets.dto;

import org.wn.rockets.entity.MissionStatus;

import java.util.List;

/**
 * Data transfer object used as response for repository.
 * @param missionName       Name of a mission.
 * @param missionStatus     Current status of a mission.
 * @param dragonsNumber     Number of rockets assigned to mission.
 * @param dragons           List of rockets assigned to mission.
 */
public record MissionSummaryDto(
        String missionName,
        MissionStatus missionStatus,
        Integer dragonsNumber,
        List<RocketSummaryDto> dragons
) {
}
