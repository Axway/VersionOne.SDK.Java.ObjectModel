/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.*;

import java.util.List;

/**
 * Filter for Test Set retrieval.
 */
public class TestSetFilter extends PrimaryWorkitemFilter{

    @Override
	public Class<? extends Entity> getEntityType() {
        return TestSet.class;
    }

    /**
     * Filter by Environment.
     */
    public final List<Environment> environment = newList();

    /**
     * Filter on related Regression suites.
     */
    public final List<RegressionSuite> regressionSuite = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.relation("RegressionSuite", regressionSuite);
        builder.relation("Environment", environment);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new IFilterTerm[]{
                        new TokenTerm("AssetState='Active';AssetType='TestSet'")
                });
            } else {
                builder.root.and(new IFilterTerm[]{
                        new TokenTerm("AssetState='Closed';AssetType='TestSet'")
                });
            }
        } else {
            builder.root.and(new IFilterTerm[]{
                    new TokenTerm("AssetState!='Dead';AssetType='TestSet'")
            });
        }
    }
}