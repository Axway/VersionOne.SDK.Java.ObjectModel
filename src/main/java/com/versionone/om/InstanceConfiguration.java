/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Represents configuration data of a VersionOne Instance.
 */
public class InstanceConfiguration {

    /**
     * True if Effort Tracking is enabled (Effort Records can be created and
     * Saved), otherwise false.
     */
    public final boolean effortTrackingEnabled;

    /**
     * The level at which DetailEstimate, ToDo and Effort are tracked for
     * Stories.
     */
    public final TrackingLevel storyTrackingLevel;

    /**
     * The level at which DetailEstimate, ToDo and Effort are tracked for
     * Defects.
     */
    public final TrackingLevel defectTrackingLevel;

    /**
     * The maximum size of an attachment the instance will accept before
     * returning an error.
     */
    public final int maximumAttachmentSize;

    InstanceConfiguration(boolean effortTrackingEnabled,
            TrackingLevel storyTrackingLevel,
            TrackingLevel defectTrackingLevel, int maxAttachmentSize) {
        this.effortTrackingEnabled = effortTrackingEnabled;
        this.defectTrackingLevel = defectTrackingLevel;
        this.storyTrackingLevel = storyTrackingLevel;
        this.maximumAttachmentSize = maxAttachmentSize;
    }
}