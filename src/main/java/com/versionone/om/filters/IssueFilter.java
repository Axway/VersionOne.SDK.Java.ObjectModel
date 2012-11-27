/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Entity;
import com.versionone.om.Issue;
import com.versionone.om.Member;
import com.versionone.om.Retrospective;
import com.versionone.om.Team;
import com.versionone.om.listvalue.IssuePriority;
import com.versionone.om.listvalue.IssueResolutionReason;
import com.versionone.om.listvalue.IssueType;

/**
 * A Filter for Issues.
 */
public class IssueFilter extends ProjectAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
	return Issue.class;
    }

    /**
     * The Issue's Type.
     */
    public final List<String> type = newList();

    /**
     * The Issue's Priority.
     */
    public final List<String> priority = newList();

    /**
     * Reason this Issue was resolved.
     */
    public final List<String> resolutionReason = newList();

    /**
     * Members that own this issue.
     */
    public final List<Member> owner = newList();

    /**
     * Teams that own this issue.
     */
    public final List<Team> team = newList();

    /**
     * Retrospectives related to this issue.
     */
    public final List<Retrospective> retrospective = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
	super.internalModifyFilter(builder);

	builder.relation("Owner", owner);
	builder.relation("Team", team);

	builder.multiRelation("Retrospectives", retrospective);

	builder.listRelation("Category", type, IssueType.class);
	builder.listRelation("Priority", priority, IssuePriority.class);
	builder.listRelation("ResolutionReason", resolutionReason,
		IssueResolutionReason.class);
    }
}
