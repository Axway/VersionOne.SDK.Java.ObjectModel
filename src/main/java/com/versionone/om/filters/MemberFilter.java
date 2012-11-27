/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Entity;
import com.versionone.om.Member;

/**
 * Filter for getting Members.
 */
public class MemberFilter extends BaseAssetFilter {

    /**
     * The short or abbreviated name of the user or member. This name is often
     * used in the owner's dropdown.
     */
    public final List<String> shortName = newList();

    /**
     * The username this user or member uses to login to the VersionOne system.
     */
    public final List<String> username = newList();

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Member.class;
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Nickname", shortName);
        builder.simple("Username", username);
    }
}
