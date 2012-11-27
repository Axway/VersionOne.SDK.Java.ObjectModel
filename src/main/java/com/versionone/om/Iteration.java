/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.DB.DateTime;
import com.versionone.om.filters.EffortFilter;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.filters.WorkitemFilter;

import java.util.Collection;

/**
 * Represents an iteration or sprint in the VersionOne System.
 */
@MetaDataAttribute("Timebox")
public class Iteration extends BaseAsset {

    /**
     * Constructor used to represent an Iteration entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Iteration(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Iteration entity that does NOT yet exist
     * in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    Iteration(V1Instance instance) {
        super(instance);
        setRelation("State", State.FUTURE);
    }

    /**
     * @return The Schedule this Iteration belongs in. This must be present.
     */
    public Schedule getSchedule() {
        return getRelation(Schedule.class, "Schedule");
    }

    /**
     * @param value The Schedule this Iteration belongs in. This must be present.
     */
    public void setSchedule(Schedule value) {
        setRelation("Schedule", value);
    }

    /**
     * @return The begin date or start date of this iteration.
     */
    public DateTime getBeginDate() {
        return new DateTime(get("BeginDate"));
    }

    /**
     * @param value The begin date or start date of this iteration.
     */
    public void setBeginDate(DateTime value) {
        set("BeginDate", value.getDate());
    }

    /**
     * @return The end date of this iteration.
     */
    public DateTime getEndDate() {
        return new DateTime(get("EndDate"));
    }

    /**
     * @param value The end date of this iteration.
     */
    public void setEndDate(DateTime value) {
        set("EndDate", value.getDate());
    }

    /**
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return A collection of stories and defects that belong to this
     *         iteration.
     */
    public Collection<PrimaryWorkitem> getPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();
        filter.iteration.clear();
        filter.iteration.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * @param filter Criteria to filter Effort records on.
     * @return A read-only collection of Effort Records that belong to this
     *         iteration.
     */
    public Collection<Effort> getEffortRecords(EffortFilter filter) {
        filter = (filter == null) ? new EffortFilter() : filter;
        filter.iteration.clear();
        filter.iteration.add(this);
        return getInstance().get().effortRecords(filter);
    }

    /**
     * @return True if this iteration is in the future state and can be opened
     *         or activated.
     */
    public boolean canActivate() {
        return getInstance().canExecuteOperation(this, "Activate");
    }

    /**
     * Open or activate this iteration. Iteration must be in a future state.
     *
     * @throws UnsupportedOperationException The item is an invalid state for
     *                 the Operation, e.g. it is already open or active.
     */
    public void activate() throws UnsupportedOperationException {
        save();
        getInstance().executeOperation(this, "Activate");
    }

    /**
     * @return True if this iteration is marked future.
     */
    public boolean isFuture() {
        return Byte.valueOf(get("AssetState").toString()) == 0;
    }

    /**
     * @return True if this iteration is open or active and can be marked
     *         future.
     */
    public boolean canMakeFuture() {
        return getInstance().canExecuteOperation(this, "MakeFuture");
    }

    /**
     * Mark this iteration as future. Iteration must be in an open or active
     * state.
     *
     * @throws UnsupportedOperationException The item is an invalid state for
     *                 the Operation, e.g. it is already in a future state.
     */
    public void makeFuture() throws UnsupportedOperationException {
        save();
        getInstance().executeOperation(this, "MakeFuture");
    }

    @Override
    boolean canCloseImpl() throws ApplicationUnavailableException {
        return getInstance().canExecuteOperation(this, "Close");
    }

    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Close");
    }

    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }

    /**
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return the total estimate for all stories and defects in this iteration
     *         optionally filtered.
     */
    public Double getTotalEstimate(PrimaryWorkitemFilter filter) {
        filter = (filter == null) ? new PrimaryWorkitemFilter() : filter;
        return getSum("Workitems:PrimaryWorkitem", filter, "Estimate");
    }

    /**
     * @param filter Criteria to filter workitems on.
     * @return the total estimate for all workitems in this iteration optionally
     *         filtered.
     */
    public Double getTotalDetailEstimate(WorkitemFilter filter) {
        filter = (filter == null) ? new WorkitemFilter() : filter;
        return getSum("Workitems", filter, "DetailEstimate");
    }

    /**
     * @param filter Criteria to filter workitems on.
     * @return the total to do for all workitems in this iteration optionally
     *         filtered.
     */
    public Double getTotalToDo(WorkitemFilter filter) {
        filter = (filter == null) ? new WorkitemFilter() : filter;
        return getSum("Workitems", filter, "ToDo");
    }

    /**
     * @param filter Criteria to filter workitems on.
     * @return the total done for all workitems in this iteration optionally
     *         filtered.
     */
    public Double getTotalDone(WorkitemFilter filter) {
        filter = (filter == null) ? new WorkitemFilter() : filter;
        return getSum("Workitems", filter, "Actuals.Value");
    }
}
