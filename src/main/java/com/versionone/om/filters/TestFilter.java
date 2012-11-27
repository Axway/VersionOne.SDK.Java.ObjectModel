/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.Entity;
import com.versionone.om.Epic;
import com.versionone.om.Test;
import com.versionone.om.listvalue.TestStatus;
import com.versionone.om.listvalue.TestType;

/**
 * Filter for Tests.
 */
public class TestFilter extends SecondaryWorkitemFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Test.class;
    }

    /**
     * The Tests' Type. Must be complete match.
     */
    public final List<String> type = newList();

    /**
     * The Status of the Test.
     */
    public final List<String> status = newList();

    /**
     * The Epic that this Test belongs to.
     */
    public final List<Epic> epic = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.listRelation("Category", type, TestType.class);
        builder.listRelation("Status", status, TestStatus.class);

        builder.relation("Parent", epic);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new TokenTerm(
                        "AssetState='Active';AssetType='Test'"));
            } else {
                builder.root.and(new TokenTerm(
                        "AssetState='Closed';AssetType='Test'"));
            }
        } else {
            builder.root.and(new TokenTerm(
                    "AssetState!='Dead';AssetType='Test'"));
        }
    }
}
