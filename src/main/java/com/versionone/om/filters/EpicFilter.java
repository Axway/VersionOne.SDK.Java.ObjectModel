package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.Entity;
import com.versionone.om.Epic;
import com.versionone.om.Goal;
import com.versionone.om.Issue;
import com.versionone.om.Request;
import com.versionone.om.Theme;
import com.versionone.om.listvalue.EpicPriority;
import com.versionone.om.listvalue.EpicStatus;
import com.versionone.om.listvalue.EpicType;
import com.versionone.om.listvalue.WorkitemRisk;
import com.versionone.om.listvalue.WorkitemSource;

/**
 * Filter for getting Epics.
 */
public class EpicFilter extends WorkitemFilter {
    @Override
	public
    Class<? extends Entity> getEntityType()
    {
        return Epic.class;
    }

    /**
     * Filter on Status.
     */
    public final List<String> status = newList();

    /**
     * Filter on Source.
     */
    public final List<String> source = newList();

    /**
     * Filter on Theme assigned.
     */
    public final List<Theme> theme = newList();

    /**
     * Filter on Risk.
     */
    public final ComparisonSearcher<Double> risk = new ComparisonSearcher<Double>();

    /**
     * Filter on Swag.
     */
    public final ComparisonSearcher<Double> swag = new ComparisonSearcher<Double>();

    /**
     * Filter on Value.
     */
    public final ComparisonSearcher<Double> value = new ComparisonSearcher<Double>();

    /**
     * Filter on Type.
     */
    public final List<String> type = newList();

    /**
     * Filter on Priority.
     */
    public final List<String> priority = newList();

    /**
     * Filter on Parent.
     */
    public final List<Epic> parent = newList();

    /**
     * Filter on Goals.
     */
    public final List<Goal> goals = newList();

    /**
     * Filter on Issues.
     */
    public final List<Issue> issues = newList();

    /**
     * Filter on blocking Issues.
     */
    public final List<Issue> blockingIssues = newList();

    /**
     * Filter on Requests.
     */
    public final List<Request> requests = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Reference", reference);

        builder.comparison("Risk", risk);
        builder.comparison("Swag", swag);
        builder.comparison("Value", value);

        builder.relation("Parent", theme);
        builder.relation("Super", parent);

        builder.multiRelation("Goals", goals);
        builder.multiRelation("Issues", issues);
        builder.multiRelation("BlockingIssues", blockingIssues);
        builder.multiRelation("Owners", owners);
        builder.multiRelation("Requests", requests);

        builder.listRelation("Status", status, EpicStatus.class);
        builder.listRelation("Source", source, WorkitemSource.class);
        builder.listRelation("Category", type, EpicType.class);
        builder.listRelation("Priority", priority, EpicPriority.class);
    }

    @Override
    //TODO investigate if this method redundant and filter can work using base class implementation
    void internalModifyState(FilterBuilder builder) {
        if(hasState()) {
            builder.root.and(isActive() ? new TokenTerm("AssetState='Active';AssetType='Epic'") :
                    new TokenTerm("AssetState='Closed';AssetType='Epic'"));
        } else {
            builder.root.and(new TokenTerm("AssetState!='Dead';AssetType='Epic'"));
        }
    }
}
