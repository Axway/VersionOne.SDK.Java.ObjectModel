/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import com.versionone.DB;
import com.versionone.om.Effort;
import com.versionone.om.Entity;
import com.versionone.om.Iteration;
import com.versionone.om.Member;
import com.versionone.om.Project;
import com.versionone.om.Team;
import com.versionone.om.Workitem;

import java.util.List;

/**
 * Filter for Effort Records.
 */
public class EffortFilter extends EntityFilter {

    /**
     * Filter on Workitem effort records belong to.
     */
    public final List<Workitem> workitem = newList();

    /**
     * Filter on Project effort records belong to.
     */
    public final List<Project> project = newList();

    /**
     * Filter on Member effort records belong to.
     */
    public final List<Member> member = newList();

    /**
     * Filter on Iteration effort records belong to.
     */
    public final List<Iteration> iteration = newList();

    /**
     * Filter on Team effort records belong to.
     */
    public final List<Team> team = newList();

    /**
     * Filter on Date range effort records belong to.
     */
    public final ComparisonSearcher<DB.DateTime> date = new DateSearcher();

    /**
     *  Filter on Value
     */
    public final ComparisonSearcher<Double> value = new ComparisonSearcher<Double>();

    /**
     * Used by the filtering code to know what types of entities to return when
     * applying the filter.
     *
     * @return class of Entity filter used for.
     */
    @Override
    public Class<? extends Entity> getEntityType() {
        return Effort.class;
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.relation("Workitem", workitem);
        builder.relation("Scope", project);
        builder.relation("Member", member);
        builder.relation("Iteration", iteration);
        builder.relation("Team", team);

        builder.comparison(Effort.ATTRIBUTE_DATE, date);
        builder.comparison(Effort.ATTRIBUTE_VALUE, value);
    }
}
