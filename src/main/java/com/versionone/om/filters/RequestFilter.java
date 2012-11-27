/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Entity;
import com.versionone.om.Issue;
import com.versionone.om.Member;
import com.versionone.om.Request;
import com.versionone.om.listvalue.RequestPriority;
import com.versionone.om.listvalue.RequestResolution;
import com.versionone.om.listvalue.RequestStatus;
import com.versionone.om.listvalue.RequestType;
import com.versionone.om.listvalue.WorkitemSource;

/**
 * A Filter for Requests.
 */
public class RequestFilter extends ProjectAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Request.class;
    }

    /**
     * The Member who owns the Request.
     */
    public final List<Member> owner = newList();

    /**
     * The Request's Source.
     */
    public final List<String> source = newList();

    /**
     * The Request's Type.
     */
    public final List<String> type = newList();

    /**
     * The Request's Status.
     */
    public final List<String> status = newList();

    /**
     * The Request's Status.
     */
    public final List<String> priority = newList();

    /**
     * Name of person or organization originating the Request.
     */
    public final List<String> requestedBy = newList();

    /**
     * Cross-reference of the Request with an external system.
     */
    public final List<String> reference = newList();

    /**
     * Reason the Request was resolved.
     */
    public final List<String> resolutionReason = newList();

    /**
     * Issues related to this Request.
     */
    public final List<Issue> issues = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.simple("RequestedBy", requestedBy);
        builder.simple("Reference", reference);

        builder.relation("Owner", owner);

        builder.multiRelation("Issues", issues);

        builder.listRelation("Source", source, WorkitemSource.class);
        builder.listRelation("Category", type, RequestType.class);
        builder.listRelation("Status", status, RequestStatus.class);
        builder.listRelation("Priority", priority, RequestPriority.class);
        builder.listRelation("ResolutionReason", resolutionReason, RequestResolution.class);
    }
}
