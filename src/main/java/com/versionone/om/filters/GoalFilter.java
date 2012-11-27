/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Entity;
import com.versionone.om.Goal;
import com.versionone.om.listvalue.GoalPriority;
import com.versionone.om.listvalue.GoalType;

/**
 * A filter for Goals.
 */
public class GoalFilter extends ProjectAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Goal.class;
    }

    /**
     * The Goal's Type.
     */
    public final List<String> type = newList();

    /**
     * The Goal's Priority.
     */
    public final List<String> priority = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.listRelation("Category", type, GoalType.class);
        builder.listRelation("Priority", priority, GoalPriority.class);
    }
}
