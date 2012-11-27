package com.versionone.om.filters;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.Entity;
import com.versionone.om.Member;
import com.versionone.om.RegressionPlan;
import com.versionone.om.RegressionSuite;

import java.util.List;

/**
 * A filter for Regression Suites.
 */
public class RegressionSuiteFilter extends BaseAssetFilter{
    @Override
	public Class<? extends Entity> getEntityType() {
        return RegressionSuite.class;
    }

    /**
     * Members that own this RegressionSuite.
     */
    public final List<Member> owners = newList();

    /**
     * Filter on Reference.
     */
    public final List<String> reference = newList();

    /**
     * DisplayID of this item.
     */
    public final List<String> displayID = newList();

    /**
     * Filter on Estimate.
     */
    public final ComparisonSearcher<Double> estimate = new ComparisonSearcher<Double>();

    /**
     * RegressionPlan associate with suite.
     */
    public final List<RegressionPlan> regressionPlan = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Reference", reference);
        builder.simple("Number", displayID);

        builder.comparison("Estimate", estimate);

        builder.relation("Owner", owners);
        builder.relation("RegressionPlan", regressionPlan);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Active';AssetType='RegressionSuite'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Closed';AssetType='RegressionSuite'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm(
                    "AssetState!='Dead';AssetType='RegressionSuite'")});
        }
    }
}