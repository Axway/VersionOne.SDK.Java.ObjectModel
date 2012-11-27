/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.BuildProject;
import com.versionone.om.BuildRun;
import com.versionone.om.ChangeSet;
import com.versionone.om.Entity;
import com.versionone.om.listvalue.BuildSource;
import com.versionone.om.listvalue.BuildStatus;

/**
 * A filter for Build Runs.
 */
public class BuildRunFilter extends BaseAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return BuildRun.class;
    }

    /**
     * Build Project of the Build Run.
     */
    public final List<BuildProject> buildProjects = newList();

    /**
     * Reference of the Build Run.
     */
    public final List<String> references = newList();

    /**
     * Source of the Build Run.
     */
    public final List<String> source = newList();

    /**
     * Status of the Build Run.
     */
    public final List<String> status = newList();

    /**
     * ChangeSets in the Build Run.
     */
    public final List<ChangeSet> changeSets = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Reference", references);

        builder.relation("BuildProject", buildProjects);
        builder.relation("ChangeSets", changeSets);

        builder.listRelation("Source", source, BuildSource.class);
        builder.listRelation("Status", status, BuildStatus.class);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {

        if (hasState()) {

            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Active';AssetType='BuildRun'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Closed';AssetType='BuildRun'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm(
                    "AssetState!='Dead';AssetType='BuildRun'")});
        }
    }
}
