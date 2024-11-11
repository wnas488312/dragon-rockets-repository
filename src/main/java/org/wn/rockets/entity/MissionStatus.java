package org.wn.rockets.entity;

import lombok.Getter;

@Getter
public enum MissionStatus {

    /**
     * Initial status, where no rockets are assigned.
     */
    SCHEDULED("Scheduled"),

    /**
     * At least one rocket is assigned and one or more assigned rockets are in repair.
     */
    PENDING("Pending"),

    /**
     * At least one rocket is assigned and none of them is in repair.
     */
    IN_PROGRESS("In Progress"),

    /**
     * The final stage of the mission, at this point rockets should not be assigned.
     * anymore to a mission
     */
    ENDED("Ended");

    private final String statusValue;

    MissionStatus(String status) {
        this.statusValue = status;
    }
}
