/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.DB.DateTime;
import com.versionone.om.Entity;
import com.versionone.om.Iteration;
import com.versionone.om.Member;
import com.versionone.om.Retrospective;
import com.versionone.om.Team;

/**
 * A filter for Retrospectives.
 */
public class RetrospectiveFilter extends ProjectAssetFilter {

    /**
     * The Iteration the Retrospective was conducted for.
     */
    public final List<Iteration> iteration = newList();

    /**
     * The Retrospective Facilitator.
     */
    public final List<Member> facilitatedBy = newList();

    /**
     * The Team the Retrospective belongs to.
     */
    public final List<Team> team = newList();

    /**
     * The date this Retrospective was conducted.  Typically stored as
     * DateTime.Date (rounded to the day).
     */
    public final List<DateTime> date = newList();

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Retrospective.class;
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("Date", date);

        builder.relation("Timebox", iteration);
        builder.relation("FacilitatedBy", facilitatedBy);
        builder.relation("Team", team);
    }
}
