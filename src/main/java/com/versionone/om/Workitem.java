/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.DB.DateTime;
import com.versionone.om.filters.EffortFilter;

import java.util.Collection;
import java.util.Map;

/**
 * Base class for Stories, Defects, Tasks, Tests.
 */
@MetaDataAttribute("Workitem")
public abstract class Workitem extends ProjectAsset {

    /**
     * Constructor used to represent an Workitem entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Workitem(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Workitem entity that does NOT yet exist
     * in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    Workitem(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Returns true if this workitem detail estimate and todo can be
     *         updated.
     */
    public boolean canTrack() {
        return getInstance().checkTracking(this);
    }

    /**
     * @return Members that own this item.
     */
    public Collection<Member> getOwners() {
        return getMultiRelation("Owners");
    }

    /**
     * @return detail's estimation.
     */
    public Double getDetailEstimate() {
        return (Double) get("DetailEstimate");
    }

    /**
     * @param detailEstimate Estimate of effort required to implement this item.
     * @throws IllegalStateException If setting DetailEstimate is not allowed at
     *                 this level.
     */
    public void setDetailEstimate(Double detailEstimate) {
        getInstance().preventTrackingLevelAbuse(this);
        set("DetailEstimate", detailEstimate);
    }

    /**
     * @return Effort already expended to implement this item.
     */
    public Double getDone() {
        return (Double) get("Actuals.Value.@Sum", false);
    }

    /**
     * @return Remaining effort required to complete implementation of this
     *         item.
     */
    public Double getToDo() {
        return (Double) get("ToDo");
    }

    /**
     * @param toDo Remaining effort required to complete implementation of this
     *                item.
     * @throws IllegalStateException if setting DetailEstimate is not allowed at
     *                 this level.
     */
    public void setToDo(Double toDo) throws IllegalStateException {
        getInstance().preventTrackingLevelAbuse(this);
        set("ToDo", toDo);
    }

    /**
     * Gets Effort Records tracked against this Workitem.
     *
     * @param filter Criteria to filter on. If null, all tasks and tests in the
     *                project are returned.
     * @return selected Effort records.
     */
    public Collection<Effort> getEffortRecords(EffortFilter filter) {
        filter = (filter == null) ? new EffortFilter() : filter;

        filter.workitem.clear();
        filter.workitem.add(this);
        return getInstance().get().effortRecords(filter);
    }

    /**
     * @return Cross-reference of this item with an external system.
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value Cross-reference of this item with an external system.
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * Log an effort record against this workitem.
     *
     * @param value of the Effort.
     * @return created Effort.
     * @throws IllegalStateException Effort Tracking is not enabled.
     */
    public Effort createEffort(double value) {
        return createEffort(value, null, null);
    }

    /**
     * Log an effort record against this workitem.
     *
     * @param value of the Effort.
     * @param attributes additional attributes for the Effort record.
     * @return created Effort.
     * @throws IllegalStateException Effort Tracking is not enabled.
     */
    public Effort createEffort(double value, Map<String, Object> attributes) {
        return createEffort(value, null, null, attributes);
    }

    /**
     * Log an effort record against this workitem with the current day and time
     * and given member and value.
     *
     * @param member The subject of the Effort.
     * @param value if the Effort.
     * @return created Effort record.
     * @throws IllegalStateException if Effort tracking is not enabled.
     */
    public Effort createEffort(double value, Member member) {
        return createEffort(value, member, DateTime.now());
    }

    /**
     * Log an effort record against this workitem with the current day and time
     * and given member and value.
     *
     * @param member The subject of the Effort.
     * @param value if the Effort.
     * @param date of the Effort record.
     * @return created Effort record.
     * @throws IllegalStateException if Effort tracking is not enabled.
     */
    public Effort createEffort(double value, Member member, DateTime date) {
        return getInstance().create().effort(value, this, member, date);
    }

    /**
     * Log an effort record against this workitem with the current day and time
     * and given member and value.
     *
     * @param member The subject of the Effort.
     * @param value if the Effort.
     * @param date of the Effort record.
     * @param attributes additional attributes for the Effort record.
     * @return created Effort record.
     * @throws IllegalStateException if Effort tracking is not enabled.
     */
    public Effort createEffort(double value, Member member, DateTime date, Map<String, Object> attributes) {
        return getInstance().create().effort(value, this, member, date, attributes);
    }
}