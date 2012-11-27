/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;
import java.util.Map;

import com.versionone.om.filters.SecondaryWorkitemFilter;
import com.versionone.om.filters.WorkitemFilter;
import com.versionone.om.listvalue.WorkitemPriority;
import com.versionone.om.listvalue.WorkitemSource;
import com.versionone.om.listvalue.WorkitemStatus;

/**
 * Base class for Stories and Defects.
 */
@MetaDataAttribute(value = "PrimaryWorkitem", defaultOrderByToken = "PrimaryWorkitem.Order")
public abstract class PrimaryWorkitem extends Workitem {

    /**
     * Constructor used to represent an PrimaryWorkitem entity that DOES exist
     * in the VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    PrimaryWorkitem(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an PrimaryWorkitem entity that does NOT yet
     * exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    PrimaryWorkitem(V1Instance instance) {
        super(instance);
    }

    /**
     * @return The Team this item is assigned to.
     */
    public Team getTeam() {
        return getRelation(Team.class, "Team");
    }

    /**
     * @param team The Team this item is assigned to.
     */
    public void setTeam(Team team) {
        setRelation("Team", team);
    }

    /**
     * @return The Iteration this item is assigned to.
     */
    @MetaRenamedAttribute("Timebox")
    public Iteration getIteration() {
        return getRelation(Iteration.class, "Timebox");
    }

    /**
     * @param iteration The Iteration this item is assigned to.
     */
    public void setIteration(Iteration iteration) {
        setRelation("Timebox", iteration);
    }

    /**
     * @return The Theme this item is assigned to.
     */
    @MetaRenamedAttribute("Parent")
    public Theme getTheme() {
        return getRelation(Theme.class, "Parent");
    }

    /**
     * @param theme The Theme this item is assigned to.
     */
    public void setTheme(Theme theme) {
        setRelation("Parent", theme);
    }

    /**
     * @return This item's Status.
     */
    public IListValueProperty getStatus() {
        return getListValue(WorkitemStatus.class, "Status");
    }

    /**
     * @return This Item's Priority.
     */
    public IListValueProperty getPriority() {
        return getListValue(WorkitemPriority.class, "Priority");
    }

    /**
     * @return This Item's Source.
     */
    public IListValueProperty getSource() {
        return getListValue(WorkitemSource.class, "Source");
    }

    /**
     * @return This item's order.
     */
    @MetaRenamedAttribute("Order")
    public Rank<PrimaryWorkitem> getRankOrder() {
        return (Rank<PrimaryWorkitem>) getRank("Order");
    }

    /**
     * @return High-level estimate (in story points) of this item.
     */
    public Double getEstimate() {
        return (Double) get("Estimate");
    }

    /**
     * @param estimate High-level estimate (in story points) of this item.
     */
    public void setEstimate(Double estimate) {
        set("Estimate", estimate);
    }

    /**
     * @return Goals this item is assigned to.
     */
    public Collection<Goal> getGoals() {
        return getMultiRelation("Goals");
    }

    /**
     * @return Requests associated with this item.
     */
    public Collection<Request> getRequests() {
        return getMultiRelation("Requests");
    }

    /**
     * @return Issues associated with this item.
     */
    public Collection<Issue> getIssues() {
        return getMultiRelation("Issues");
    }

    /**
     * @return Issues that are preventing the completion of this item.
     */
    public Collection<Issue> getBlockingIssues() {
        return getMultiRelation("BlockingIssues");
    }

    /**
     * @return Defects affecting this item.
     */
    public Collection<Defect> getAffectedByDefects() {
        return getMultiRelation("AffectedByDefects");
    }

    /**
     * @return Build Run's this Primary Workitem was completed in.
     */
    @MetaRenamedAttribute("CompletedInBuildRuns")
    public Collection<BuildRun> getCompletedIn() {
        return getMultiRelation("CompletedInBuildRuns");
    }

    /**
     * Create a task that belongs to this item.
     *
     * @param name The name of the task.
     * @return created task.
     */
    public Task createTask(String name) {
        return getInstance().create().task(name, this);
    }

    /**
     * Create a task that belongs to this item.
     *
     * @param name The name of the task.
     * @param attributes additional attributes for task.
     * @return created task.
     */
    public Task createTask(String name, Map<String, Object> attributes) {
        return getInstance().create().task(name, this, attributes);
    }


    /**
     * Create a test that belongs to this item.
     *
     * @param name The name of the test.
     * @return created test.
     */
    public Test createTest(String name) {
        return getInstance().create().test(name, this);
    }

    /**
     * Create a test that belongs to this item.
     *
     * @param name The name of the test.
     * @param attributes additional attributes for test.
     * @return created test.
     */
    public Test createTest(String name, Map<String, Object> attributes) {
        return getInstance().create().test(name, this, attributes);
    }

    /**
     * Gets the estimate of total effort required to implement this item.
     *
     * @param filter filter for Workitems (If null, then all items returned).
     * @return estimate of total effort required to implement this item.
     */
    public Double getTotalDetailEstimate(WorkitemFilter filter) {
        return getSum("ChildrenMeAndDown", (filter != null) ? filter
                : new WorkitemFilter(), "DetailEstimate");
    }

    /**
     * Gets the total effort already expended to implement this item.
     *
     * @param filter restriction for the work items which have to be counted. If
     *                null, then all items counted.
     * @return total effort already expended to implement this item.
     */
    public Double getTotalDone(WorkitemFilter filter) {
        return getSum("ChildrenMeAndDown", (filter != null) ? filter
                : new WorkitemFilter(), "Actuals.Value");
    }

    /**
     * Gets the total remaining effort required to complete implementation of
     * this item.
     *
     * @param filter restriction for the work items which have to be counted. If
     *                null, then all items counted.
     * @return total remaining effort required to complete implementation of
     *         this item.
     */
    public Double getTotalToDo(WorkitemFilter filter) {
        return getSum("ChildrenMeAndDown", (filter != null) ? filter
                : new WorkitemFilter(), "ToDo");
    }

    /**
     * Collection of Tasks and Tests that belong to this primary workitem.
     *
     * @param filter How to filter the secondary workitems. To get only Tasks,
     *                pass a TaskFilter. To get only Tests, pass a TestFilter.
     * @return Collection of Tasks and Tests that belong to this primary
     *         workitem.
     */
    public Collection<SecondaryWorkitem> getSecondaryWorkitems(
            SecondaryWorkitemFilter filter) {

        if (filter == null) {
            filter = new SecondaryWorkitemFilter();
        }

        filter.parent.clear();
        filter.parent.add(this);

        return getInstance().get().secondaryWorkitems(filter);
    }
}
