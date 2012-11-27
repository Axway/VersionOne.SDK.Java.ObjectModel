/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Entity;
import com.versionone.om.Goal;
import com.versionone.om.Member;
import com.versionone.om.Theme;
import com.versionone.om.listvalue.ThemeSource;
import com.versionone.om.listvalue.ThemeStatus;
import com.versionone.om.listvalue.ThemeType;
import com.versionone.om.listvalue.WorkitemPriority;
import com.versionone.om.listvalue.WorkitemRisk;

/**
 * Filter for getting themes.
 */
public class ThemeFilter extends ProjectAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Theme.class;
    }

    /**
     * Parent theme's.
     */
    public final List<Theme> parent = newList();

    /**
     * Filter on Status.
     */
    public final List<String> status = newList();

    /**
     * Filter on Source.
     */
    public final List<String> source = newList();

    /**
     * Filter on Theme assigned.
     */
    public final List<Theme> theme = newList();

    /**
     * Filter on Customer.
     */
    public final List<Member> customer = newList();

    /**
     * Filter on Risk.
     */
    public final List<String> risk = newList();

    /**
     * Filter on Type.
     */
    public final List<String> type = newList();

    /**
     * Filter on Priority.
     */
    public final List<String> priority = newList();

    /**
     * Filter on Goals.
     */
    public final List<Goal> goals = newList();

    /**
     * Filter on Owners.
     */
    public final List<Member> owners = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.relation("Parent", parent);
        builder.relation("Customer", customer);
        builder.relation("Theme", theme);

        builder.multiRelation("Goals", goals);
        builder.multiRelation("Owners", owners);

        builder.listRelation("Status", status, ThemeStatus.class);
        builder.listRelation("Source", source, ThemeSource.class);
        builder.listRelation("Risk", risk, WorkitemRisk.class);
        builder.listRelation("Category", type, ThemeType.class);
        builder.listRelation("Priority", priority, WorkitemPriority.class);
    }
}
