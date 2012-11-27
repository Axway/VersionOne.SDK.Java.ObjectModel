/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.DB.DateTime;
import com.versionone.om.filters.DefectFilter;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.listvalue.BuildSource;
import com.versionone.om.listvalue.BuildStatus;

import java.util.Collection;

/**
 * Represents a Build Run in the VersionOne System.
 */
@MetaDataAttribute("BuildRun")
public class BuildRun extends BaseAsset {

    /**
     * Constructor used to represent an BuildRun entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    BuildRun(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an BaseAsset BuildRun that does NOT yet
     * exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    BuildRun(V1Instance instance) {
        super(instance);
    }

    /**
     * @return The Build Project this Build Run belongs to.
     */
    public BuildProject getBuildProject() {
        return getRelation(BuildProject.class, "BuildProject");
    }

    /**
     * @param value The Build Project this Build Run belongs to.
     */
    public void setBuildProject(BuildProject value) {
        setRelation("BuildProject", value);
    }

    /**
     * @return The Date this Build Run ran.
     */
    public DateTime getDate() {
        return new DateTime(get("Date"));
    }

    /**
     * @param value The Date this Build Run ran.
     */
    public void setDate(DateTime value) {
        set("Date", value);
    }

    /**
     * @return The total elapsed time of the Build Run.
     */
    public Double getElapsed() {
        return (Double) get("Elapsed");
    }

    /**
     * @param value The total elapsed time of the Build Run.
     */
    public void setElapsed(Double value) {
        set("Elapsed", value);
    }

    /**
     * @return A reference to an external system.
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value A reference to an external system.
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * @return The source of this Build Run.
     */
    public IListValueProperty getSource() {
        return getListValue(BuildSource.class, "Source");
    }

    /**
     * @return The status of this Build Run.
     */
    public IListValueProperty getStatus() {
        return getListValue(BuildStatus.class, "Status");
    }

    /**
     * @return ChangeSets in this BuildRun.
     */
    public Collection<ChangeSet> getChangeSets() {
        return getMultiRelation("ChangeSets");// ChangeSet
    }

    /**
     * Stories and Defects completed in this Build Run.
     *
     * @param filter Filter for PrimaryWorkitems.
     * @return completed Stories and Defects completed in this Build Run.
     */
    public Collection<PrimaryWorkitem> getCompletedPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        filter.completedIn.clear();
        filter.completedIn.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * @param filter Filter for PrimaryWorkitems.
     * @return Stories and Defects with source changes in this Build Run.
     */
    public Collection<PrimaryWorkitem> getAffectedPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();
        // The BuildRun's connected to the ChangeSets.
        Collection<ChangeSet> changeSets = getChangeSets();

        for (ChangeSet changeSet : changeSets) {

            // The ChangeSet's connected to the PWI's. Sing with me, now!
            for (PrimaryWorkitem pwi : changeSet.getPrimaryWorkitems()) {
                // Add the specific items by ID.
                filter.displayID.add(pwi.getDisplayID());
            }
        }
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * Defects found in this Build Run.
     *
     * @param filter Limit the defect returned (If null, then all items
     *                returned).
     * @return collection of defects.
     */
    public Collection<Defect> getFoundDefects(DefectFilter filter) {
        filter = (filter != null) ? filter : new DefectFilter();

        filter.foundIn.clear();
        filter.foundIn.add(this);
        return getInstance().get().defects(filter);
    }

    @Override
    boolean canCloseImpl() {
        return false;
    }

    @Override
    boolean canReactivateImpl() {
        return false;
    }

    @Override
    void closeImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot close build runs");
    }

    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot reactivate build runs");
    }
}
