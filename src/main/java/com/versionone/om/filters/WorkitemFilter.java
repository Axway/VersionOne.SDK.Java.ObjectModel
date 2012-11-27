package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.Entity;
import com.versionone.om.Member;
import com.versionone.om.Workitem;

/**
 * Filter for Workitems.
 */
public class WorkitemFilter extends ProjectAssetFilter {
    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Workitem.class;
    }

    /**
     * Item owner(s).
     */
    public final List<Member> owners = newList();

    /**
     * Filter on Reference.
     */
    public final List<String> reference = newList();

    /**
     * Filter on DetailEstimate. Must be an exact match. Add a null to the list
     * to include items that have not been estimated.
     */
    public final ComparisonSearcher<Double> detailEstimate = new ComparisonSearcher<Double>();

    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.multiRelation("Owners", owners);
        builder.simple("Reference", reference);

        builder.comparison("DetailEstimate", detailEstimate);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm("AssetState='Active';AssetType!='Theme'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm("AssetState='Closed';AssetType!='Theme'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm("AssetState!='Dead';AssetType!='Theme'")});
        }
    }
}
