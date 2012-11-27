/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.Entity;
import com.versionone.om.Workitem;
import com.versionone.om.SecondaryWorkitem;

/**
 * Filter for Tasks and Tests.
 */
public class SecondaryWorkitemFilter extends WorkitemFilter {

    /**
     * Filter on which Story or Defect this workitem is attached to.
     */
    public final List<Workitem> parent = newList();
   
    @Override
	public Class<? extends Entity> getEntityType() {
		return SecondaryWorkitem.class;
	}

	@Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.relation("Parent", parent);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Active';AssetType='Task','Test'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm(
                        "AssetState='Closed';AssetType='Task','Test'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm(
                    "AssetState!='Dead';AssetType='Task','Test'")});
        }
    }
}
