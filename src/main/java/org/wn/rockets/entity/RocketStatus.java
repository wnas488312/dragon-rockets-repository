package org.wn.rockets.entity;

import lombok.Getter;

@Getter
public enum RocketStatus {

    /**
     * Initial status, where the rocket is not assigned to any mission.
     */
    ON_GROUND("On ground"),

    /**
     * The rocket was assigned to the mission.
     */
    IN_SPACE("In space"),

    /**
     * The rocket is due to repair, it implies “Pending” status of the mission.
     */
    IN_REPAIR("In repair");

    private final String statusValue;

    RocketStatus(String status) {
        this.statusValue = status;
    }
}
