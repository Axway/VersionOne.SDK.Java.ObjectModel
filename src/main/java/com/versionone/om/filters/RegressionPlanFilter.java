/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.Entity;
import com.versionone.om.Member;
import com.versionone.om.RegressionPlan;

import java.util.List;

/**
 * A filter for Regression Plans.
 */
public class RegressionPlanFilter extends ProjectAssetFilter{

    @Override
	public Class<? extends Entity> getEntityType() {
        return RegressionPlan.class;
    }

    /**
     * Members that own this RegressionPlan.
     */
    public final List<Member> owners = newList();

    /**
     * Filter on Reference.
     */
    public final List<String> reference = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.relation("Owner", owners);
        builder.simple("Reference", reference);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Active';AssetType='RegressionPlan'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Closed';AssetType='RegressionPlan'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm(
                    "AssetState!='Dead';AssetType='RegressionPlan'")});
        }
    }
}