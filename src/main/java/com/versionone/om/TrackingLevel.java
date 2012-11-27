/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * The level at which DetailEstimate, ToDo and Effort are tracked.
 */
public enum TrackingLevel {
    /**
     * Track Detail Estimate and ToDo at PrimaryWorkitem (Story/Defect)
     * level only.
     */
    PrimaryWorkitem(com.versionone.apiclient.IV1Configuration.TrackingLevel.On),
    /**
     * Track Detail Estimate and ToDo at SecondaryWorkitem (Task/Test)
     * level only.
     */
    SecondaryWorkitem(
            com.versionone.apiclient.IV1Configuration.TrackingLevel.Off),
    /**
     * Track Detail Estimate and ToDo at both the PrimaryWorkitem and
     * SecondaryWorkitem levels.
     */
    Both(com.versionone.apiclient.IV1Configuration.TrackingLevel.Mix);

    private final com.versionone.apiclient.IV1Configuration.TrackingLevel apiTrackingLevel;

    private TrackingLevel(
            com.versionone.apiclient.IV1Configuration.TrackingLevel apiTrackingLevel) {
        this.apiTrackingLevel = apiTrackingLevel;
    }

    /**
     * Converts tracking level from ACPIClient to Object Model.
     *
     * @param apiTrackingLevel tracking level used in the APIClient.
     * @return tracking level used in the Object Model.
     */
    public static TrackingLevel valueOf(
            com.versionone.apiclient.IV1Configuration.TrackingLevel apiTrackingLevel) {

        for (TrackingLevel level : values()) {

            if (level.apiTrackingLevel == apiTrackingLevel) {
                return level;
            }
        }
        return null;
    }
}
