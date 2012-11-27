/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.BuildRun;
import com.versionone.om.Defect;
import com.versionone.om.Entity;
import com.versionone.om.Member;
import com.versionone.om.PrimaryWorkitem;
import com.versionone.om.listvalue.DefectResolutionReason;
import com.versionone.om.listvalue.DefectType;

/**
 * Filter for getting defects.
 */
public class DefectFilter extends PrimaryWorkitemFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Defect.class;
    }

    /**
     * Filter on the Type property.
     */
    public final List<String> type = newList();

    /**
     * Filter on the FoundBy property.
     */
    public final List<String> foundBy = newList();

    /**
     * Filter on the FoundInBuild property.
     */
    public final List<String> foundInBuild = newList();

    /**
     * Filter on the FoundInVersion property.
     */
    public final List<String> foundInVersion = newList();

    /**
     * Filter on the Environment property.
     */
    public final List<String> environment = newList();

    /**
     * Filter on the ResolvedInBuild property.
     */
    public final List<String> resolvedInBuild = newList();

    /**
     * Filter on the ResolutionReason property.
     */
    public final List<String> resolutionReason = newList();

    /**
     * Filter on the VerifiedBy property.
     */
    public final List<Member> verifiedBy = newList();

    /**
     * Filter on stories or defects affected.
     */
    public final List<PrimaryWorkitem> affectedPrimaryWorkitems = newList();

    /**
     * Filter on build runs.
     */
    public final List<BuildRun> foundIn = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("FoundBy", foundBy);
        builder.simple("FoundInBuild", foundInBuild);
        builder.simple("FoundInVersion", foundInVersion);
        builder.simple("Environment", environment);
        builder.simple("FixedInBuild", resolvedInBuild);

        builder.relation("VerifiedBy", verifiedBy);

        builder.multiRelation("AffectedPrimaryWorkitems",
                affectedPrimaryWorkitems);
        builder.multiRelation("FoundInBuildRuns", foundIn);

        builder.listRelation("Type", type, DefectType.class);
        builder.listRelation("ResolutionReason", resolutionReason,
                DefectResolutionReason.class);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {

        if (hasState()) {

            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Active';AssetType='Defect'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Closed';AssetType='Defect'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm(
                    "AssetState!='Dead';AssetType='Defect'")});
        }
    }
}
