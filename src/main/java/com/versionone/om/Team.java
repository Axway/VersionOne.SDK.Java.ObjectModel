/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;

import com.versionone.om.filters.EffortFilter;
import com.versionone.om.filters.IssueFilter;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.filters.RetrospectiveFilter;
import com.versionone.om.filters.WorkitemFilter;

/**
 * Represents a team in the VersionOne system.
 */
@MetaDataAttribute("Team")
public class Team extends BaseAsset {

    Team(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    Team(V1Instance instance) {
        super(instance);
    }

    /**
     * Gets the Stories and Defects assigned to this Team.
     *
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return assigned Stories and Defects.
     */
    public Collection<PrimaryWorkitem> getPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        filter = (filter == null) ? new PrimaryWorkitemFilter() : filter;

        filter.team.clear();
        filter.team.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * Gets the Issues assigned to this Team.
     *
     * @param filter Criteria to filter Issues on.
     * @return assigned Issues.
     */
    public Collection<Issue> getIssues(IssueFilter filter) {
        filter = (filter == null) ? new IssueFilter() : filter;

        filter.team.clear();
        filter.team.add(this);
        return getInstance().get().issues(filter);
    }

    /**
     * Gets the Retrospectives assigned to this Team.
     *
     * @param filter Criteria to filter Retrospectives on.
     * @return assigned Retrospectives.
     */
    public Collection<Retrospective> getRetrospectives(
            RetrospectiveFilter filter) {
        filter = (filter == null) ? new RetrospectiveFilter() : filter;

        filter.team.clear();
        filter.team.add(this);
        return getInstance().get().retrospectives(filter);
    }

    /**
     * Gets Effort records that belong to this Team.
     *
     * @param filter Criteria to filter Effort records on.
     * @return belonged Effort records.
     */
    public Collection<Effort> getEffortRecords(EffortFilter filter) {
        filter = (filter == null) ? new EffortFilter() : filter;

        filter.team.clear();
        filter.team.add(this);
        return getInstance().get().effortRecords(filter);
    }

    /**
     * Inactivates the Team.
     *
     * @throws UnsupportedOperationException The Team is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Team.
     *
     * @throws UnsupportedOperationException The Theme is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }

    /**
     * Return the total estimate for all stories and defects in this team
     * optionally filtered.
     *
     * @param filter Criteria to filter stories and defects on.
     * @return sum of Estimates of selected Stories and Defects.
     */
    public Double getTotalEstimate(PrimaryWorkitemFilter filter) {
        filter = (filter == null) ? new PrimaryWorkitemFilter() : filter;

        return getSum("Workitems:PrimaryWorkitem", filter, "Estimate");
    }

    /**
     * Return the total done for all workitems in this team optionally filtered.
     *
     * @param filter Criteria to filter workitems on.
     * @return sum of Done of selected workitems.
     */
    public Double getTotalDone(WorkitemFilter filter) {
        filter = (filter == null) ? new WorkitemFilter() : filter;

        return getSum("Workitems", filter, "Actuals.Value");
    }

    /**
     * Return the total detail estimate for all Workitems in this team
     * optionally filtered.
     *
     * @param filter Criteria to filter Workitems on.
     * @return sum of DetailEstimates of selected Workitems.
     */
    public Double getTotalDetailEstimate(WorkitemFilter filter) {
        filter = (filter == null) ? new WorkitemFilter() : filter;

        return getSum("Workitems", filter, "DetailEstimate");
    }

    /**
     * Return the total Todo for all workitems in this team optionally filtered.
     *
     * @param filter Criteria to filter Workitems on.
     * @return sum of Todo of selected Workitems.
     */
    public Double getTotalToDo(WorkitemFilter filter) {
        filter = (filter == null) ? new WorkitemFilter() : filter;

        return getSum("Workitems", filter, "ToDo");
    }
}
