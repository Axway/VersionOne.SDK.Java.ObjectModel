/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;
import com.versionone.Duration;

/**
 * Represents a schedule in the VersionOne system
 */
@MetaDataAttribute("Schedule")
public class Schedule extends BaseAsset 
{
	
    /**
     * Constructor used to represent an Schedule entity that does NOT yet exist
     * in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    Schedule(V1Instance instance) {
        super(instance);
    }
	
    /**
     * Constructor used to represent an Project entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Schedule(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Projects associated with this Schedule
     */
    public Collection<Project> getScheduledProjects()
    { 
    	return getMultiRelation("ScheduledScopes"); 
    }

    /**
     * @return The number of days an iteration will last.
     */
    public Duration getIterationLength() {
    	return (Duration) get("TimeboxLength");
    }

    /**
     * @param value The number of days an iteration will last.
     */
    public void setIterationLength(Duration value) {
    	set("TimeboxLength", value);
    }

    /**
     * @return The number of days an iteration will last.
     */
    public Duration getIterationGap() {
    	return (Duration) get("TimeboxGap");
    }

    /**
     * @param value The number of days between iterations.
     */
    public void setIterationGap(Duration value) {
    	set("TimeboxGap", value);
    }

    /**
     * Inactivates the Schedule
     * 
     * @exception InvalidOperationException The Schedule is an invalid state for the Operation, e.g. it is already closed.
     */
	@Override
	void closeImpl() throws UnsupportedOperationException {
		getInstance().executeOperation(this, "Inactivate");
	}

	/**
	 * Reactivates the Schedule
	 * @exception InvalidOperationException The Schedule is an invalid state for the Operation, e.g. it is already active.
	 */
	@Override
	void reactivateImpl() throws UnsupportedOperationException {
		getInstance().executeOperation(this, "Reactivate");
	}	
}