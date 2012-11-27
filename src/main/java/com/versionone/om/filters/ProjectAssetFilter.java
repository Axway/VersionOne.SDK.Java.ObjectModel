/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Entity;
import com.versionone.om.Project;
import com.versionone.om.ProjectAsset;

/**
 * Filter on Project Assets.
 */
public abstract class ProjectAssetFilter extends BaseAssetFilter {

    /**
     * Project this item belongs to.
     */
    public final List<Project> project = newList();

    /**
     * DisplayID of this item.
     */
    public final List<String> displayID = newList();

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return ProjectAsset.class;
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.relation("Scope", project);
        builder.simple("Number", displayID);
    }
}
