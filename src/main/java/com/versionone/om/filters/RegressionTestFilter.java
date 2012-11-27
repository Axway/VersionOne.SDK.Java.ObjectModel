package com.versionone.om.filters;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.*;
import com.versionone.om.listvalue.TestType;

import java.util.List;

/**
 * A filter for Regression Test.
 */
public class RegressionTestFilter extends ProjectAssetFilter {
    
    @Override
    public Class<? extends Entity> getEntityType() {
        return RegressionTest.class;
    }

    /**
     * Test which was used to generate this item.
     */
    public final List<Test> generatedFrom = newList();

    /**
     * Tests generated from this item.
     */
    public final List<Test> generatedTests = newList();

    /**
     * Tags of this item.
     */
    public final List<String> tags = newList();

    /**
     * Related Regression Suites.
     */
    public final List<RegressionSuite> regressionSuites = newList();

    /**
     * Item owner(s).
     */
    public final List<Member> owners = newList();

    /**
     * Item status.
     */
    public final List<String> status = newList();

    /**
     * Item category.
     */
    public final List<String> type = newList();

    /**
     * Reference of this item.
     */
    public final List<String> reference = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Tags", tags);
        builder.simple("Reference", reference);

        builder.relation("GeneratedFrom", generatedFrom);

        builder.multiRelation("RegressionSuites", regressionSuites);
        builder.multiRelation("Owners", owners);
        builder.multiRelation("GeneratedTests", generatedTests);

        builder.listRelation("Status", status, RegressionTestStatus.class);
        builder.listRelation("Category", type, TestType.class);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Active';AssetType='RegressionTest'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Closed';AssetType='RegressionTest'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm(
                    "AssetState!='Dead';AssetType='RegressionTest'")});
        }
    }
}
