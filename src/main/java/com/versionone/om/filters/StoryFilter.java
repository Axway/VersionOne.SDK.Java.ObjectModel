/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.Entity;
import com.versionone.om.Epic;
import com.versionone.om.Member;
import com.versionone.om.Retrospective;
import com.versionone.om.Story;
import com.versionone.om.listvalue.StoryType;
import com.versionone.om.listvalue.WorkitemRisk;

/**
 * Filter for getting stories.
 */
public class StoryFilter extends PrimaryWorkitemFilter {
    /**
     * Name of person or organization requesting this Story. Must be complete
     * match.
     */
    public final List<String> requestedBy = newList();

    /**
     * Build number associated with this Story. Must be complete match.
     */
    public final List<String> build = newList();

    /**
     * This Story's Risk.
     */
    public final List<String> risk = newList();

    /**
     * This Story's Type.
     */
    public final List<String> type = newList();

    /**
     * Member assigned as a customer for this Story.
     */
    public final List<Member> customer = newList();

    /**
     * Stories that this Story depends on.
     */
    public final List<Story> dependsOnStories = newList();

    /**
     * Stories that depend on this Story.
     */
    public final List<Story> dependentStories = newList();

    /**
     * The (optional) Retrospective this Story was Identified in.
     */
    public final List<Retrospective> identifiedIn = newList();

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Story.class;
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("RequestedBy", requestedBy);
        builder.simple("LastVersion", build);
        builder.relation("Customer", customer);
        builder.relation("Dependencies", dependsOnStories);
        builder.relation("IdentifiedIn", identifiedIn);

        builder.multiRelation("Dependants", dependentStories);

        builder.listRelation("Risk", risk, WorkitemRisk.class);
        builder.listRelation("Category", type, StoryType.class);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new TokenTerm(
                        "AssetState='Active';AssetType='Story'"));
            } else {
                builder.root.and(new TokenTerm(
                        "AssetState='Closed';AssetType='Story'"));
            }
        } else {
            builder.root.and(new TokenTerm(
                    "AssetState!='Dead';AssetType='Story'"));
        }
    }
}
