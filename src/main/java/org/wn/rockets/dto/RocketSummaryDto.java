package org.wn.rockets.dto;

import org.wn.rockets.entity.RocketStatus;

/**
 * Data transfer object used as response for repository.
 * @param rocketName            Name of a rocket.
 * @param rocketStatus          Current status of a rocket.
 */
public record RocketSummaryDto (
        String rocketName,
        RocketStatus rocketStatus
) {
}
