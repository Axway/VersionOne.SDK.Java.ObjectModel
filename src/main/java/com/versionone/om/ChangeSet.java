/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;

import com.versionone.om.filters.BuildRunFilter;

/**
 * Represents a ChangeSet in the VersionOne system.
 */
@MetaDataAttribute("ChangeSet")
public class ChangeSet extends BaseAsset {

    /**
     * Constructor used to represent an ChangeSet entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    ChangeSet(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an ChangeSet entity that does NOT yet exist
     * in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    ChangeSet(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Reference of this ChangeSet.
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value Reference of this ChangeSet.
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * @return Primary workitems affected by this ChangeSet.
     */
    public Collection<PrimaryWorkitem> getPrimaryWorkitems() {
        return getMultiRelation("PrimaryWorkitems");
    }

    /**
     * @param filter filter for getting collection of Build Runs.
     * @return A collection of Build Runs associated with this ChangeSet.
     */
    public Collection<BuildRun> getBuildRuns(BuildRunFilter filter) {
        filter = (filter != null) ? filter : new BuildRunFilter();

        filter.changeSets.clear();
        filter.changeSets.add(this);
        return getInstance().get().buildRuns(filter);
    }

    /**
     * Closes the ChangeSet.
     *
     * @throws UnsupportedOperationException throws always because of ChangeSets
     *                 cannot be closed in VersionOne.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "ChangeSets cannot be closed in VersionOne.");
    }

    /**
     * Reopens the ChangeSet.
     *
     * @throws UnsupportedOperationException throws always because of ChangeSets
     *                 cannot be reactivated in VersionOne.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "ChangeSets cannot be reactivated in VersionOne.");
    }

    @Override
    boolean canCloseImpl() {
        return false;
    }
}
