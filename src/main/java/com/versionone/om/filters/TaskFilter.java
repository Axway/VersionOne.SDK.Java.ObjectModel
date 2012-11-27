/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.Entity;
import com.versionone.om.Task;
import com.versionone.om.listvalue.TaskSource;
import com.versionone.om.listvalue.TaskStatus;
import com.versionone.om.listvalue.TaskType;

/**
 * Filter for Tasks.
 */
public class TaskFilter extends SecondaryWorkitemFilter {
    /**
     * Build number associated with the Task. Must be complete match.
     */
    public final List<String> build = newList();

    /**
     * The Source of this Task.
     */
    public final List<String> source = newList();

    /**
     * The Tasks's Type. Must be complete match.
     */
    public final List<String> type = newList();

    /**
     * The Status of the Task.
     */
    public final List<String> status = newList();

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Task.class;
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("LastVersion", build);

        builder.listRelation("Source", source, TaskSource.class);
        builder.listRelation("Category", type, TaskType.class);
        builder.listRelation("Status", status, TaskStatus.class);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new TokenTerm(
                        "AssetState='Active';AssetType='Task'"));
            } else {
                builder.root.and(new TokenTerm(
                        "AssetState='Closed';AssetType='Task'"));
            }
        } else {
            builder.root.and(new TokenTerm(
                    "AssetState!='Dead';AssetType='Task'"));
        }
    }
}
