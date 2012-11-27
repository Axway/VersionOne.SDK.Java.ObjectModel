/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;


import java.util.List;

import com.versionone.om.BaseAsset;
import com.versionone.om.Entity;
import com.versionone.om.Link;

/**
 * A filter for Links.
 */
public class LinkFilter extends EntityFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Link.class;
    }

    /**
     * Item name. Must be complete match.
     */
    public final List<String> name = newList();

    /**
     * Item URL. Must be complete match.
     */
    public final List<String> url = newList();

    /**
     * The Link's Related Asset.
     */
    public final List<BaseAsset> asset = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Name", name);
        builder.simple("URL", url);

        builder.relation("Asset", asset);
    }
}
