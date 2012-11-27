package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.BuildRun;
import com.versionone.om.Defect;
import com.versionone.om.Entity;
import com.versionone.om.Epic;
import com.versionone.om.Goal;
import com.versionone.om.Issue;
import com.versionone.om.Iteration;
import com.versionone.om.PrimaryWorkitem;
import com.versionone.om.Request;
import com.versionone.om.Team;
import com.versionone.om.Theme;
import com.versionone.om.listvalue.WorkitemPriority;
import com.versionone.om.listvalue.WorkitemSource;
import com.versionone.om.listvalue.WorkitemStatus;

/**
 * Filter for PrimaryWorkitems.
 */
public class PrimaryWorkitemFilter extends WorkitemFilter {
    /**
     * Filter on Story status.
     */
    public final List<String> status = newList();

    /**
     * Filter on Team assigned.
     */
    public final List<Team> team = newList();

    /**
     * Filter on Iteration this item is in.
     */
    public final List<Iteration> iteration = newList();

    /**
     * Filter on Theme assigned.
     */
    public final List<Theme> theme = newList();

    /**
     * Filter on Goals.
     */
    public final List<Goal> goals = newList();

    /**
     * Filter on Requests.
     */
    public final List<Request> requests = newList();

    /**
     * Filter on Issues.
     */
    public final List<Issue> issues = newList();

    /**
     * Filter on BlockingIssues.
     */
    public final List<Issue> blockingIssues = newList();

    /**
     * Filter on AffectedByDefects.
     */
    public final List<Defect> affectedByDefects = newList();

    /**
     * Filter on Estimate. Must be an exact match. Add a null to the list to
     * include items that have not been estimated.
     */
    public final ComparisonSearcher<Double> estimate = new ComparisonSearcher<Double>();

    /**
     * Filter on Source.
     */
    public final List<String> source = newList();

    /**
     * Filter on Priority.
     */
    public final List<String> priority = newList();

    /**
     * Filter on Build Runs.
     */
    public final List<BuildRun> completedIn = newList();
    
    public final List<Epic> epic = newList();
    

    @Override
	public Class<? extends Entity> getEntityType() {
        return PrimaryWorkitem.class;
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.comparison("Estimate", estimate);

        builder.relation("Team", team);
        builder.relation("Parent", theme);
        builder.relation("Timebox", iteration);
        builder.relation("Super", epic);

        builder.listRelation("Status", status, WorkitemStatus.class);
        builder.listRelation("Source", source, WorkitemSource.class);
        builder.listRelation("Priority", priority, WorkitemPriority.class);

        builder.multiRelation("Goals", goals);
        builder.multiRelation("Requests", requests);
        builder.multiRelation("Issues", issues);
        builder.multiRelation("BlockingIssues", blockingIssues);
        builder.multiRelation("AffectedByDefects", affectedByDefects);
        builder.multiRelation("CompletedInBuildRuns", completedIn);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Active';AssetType='Story','Defect','TestSet'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Closed';AssetType='Story','Defect','TestSet'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm(
                    "AssetState!='Dead';AssetType='Story','Defect','TestSet'")});
        }
    }
}
