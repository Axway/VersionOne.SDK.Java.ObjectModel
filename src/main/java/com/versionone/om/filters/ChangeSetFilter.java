/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.ChangeSet;
import com.versionone.om.Entity;

/**
 * A filter for ChangeSets.
 */
public class ChangeSetFilter extends BaseAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return ChangeSet.class;
    }

    /**
     * Cross-reference of the ChangeSet with an external system.
     */
    public final List<String> reference = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Reference", reference);
    }
}
