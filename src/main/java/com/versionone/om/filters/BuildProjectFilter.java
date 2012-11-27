/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.BuildProject;
import com.versionone.om.Entity;

/**
 * A filter for Build Projects.
 */
public class BuildProjectFilter extends BaseAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return BuildProject.class;
    }

    /**
     * Reference of the Build Project.
     */
    public final List<String> references = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Reference", references);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {

        if (hasState()) {

            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Active';AssetType='BuildProject'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Closed';AssetType='BuildProject'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm(
                    "AssetState!='Dead';AssetType='BuildProject'")});
        }
    }

}
