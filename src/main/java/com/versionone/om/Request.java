/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;
import java.util.Map;

import com.versionone.om.filters.EpicFilter;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.listvalue.RequestPriority;
import com.versionone.om.listvalue.RequestResolution;
import com.versionone.om.listvalue.RequestStatus;
import com.versionone.om.listvalue.RequestType;
import com.versionone.om.listvalue.WorkitemSource;

/**
 * Represents a request in the VersionOne system.
 */
@MetaDataAttribute("Request")
public class Request extends ProjectAsset {

    /**
     * Constructor used to represent an Request entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Request(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Request entity that does NOT yet exist
     * in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    Request(V1Instance instance) {
        super(instance);
    }

    /**
     * @return The Member who owns this Request.
     */
    public Member getOwner() {
        return getRelation(Member.class, "Owner");
    }

    /**
     * @param value The Member who owns this Request.
     */
    public void setOwner(Member value) {
        setRelation("Owner", value);
    }

    /**
     * @return This Request's rank order among all Requests.
     */
    @MetaRenamedAttribute("Order")
    public Rank<Request> getRankOrder() {
        return (Rank<Request>) getRank("Order");
    }

    /**
     * @return This Request's Source.
     */
    public IListValueProperty getSource() {
        return getListValue(WorkitemSource.class, "Source");
    }

    /**
     * @return This Request's Type.
     */
    public IListValueProperty getType() {
        return getListValue(RequestType.class, "Category");
    }

    /**
     * @return This Request's Status.
     */
    public IListValueProperty getStatus() {
        return getListValue(RequestStatus.class, "Status");
    }

    /**
     * @return This Request's Priority.
     */
    public IListValueProperty getPriority() {
        return getListValue(RequestPriority.class, "Priority");
    }

    /**
     * @return Name of person or organization originating this Request.
     */
    public String getRequestedBy() {
        return (String) get("RequestedBy");
    }

    /**
     * @param value Name of person or organization originating this Request.
     */
    public void setRequestedBy(String value) {
        set("RequestedBy", value);
    }

    /**
     * @return Cross-reference of this Request with an external system.
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value Cross-reference of this Request with an external system.
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * @return Reason this Request was resolved.
     */
    public IListValueProperty getResolutionReason() {
        return getListValue(RequestResolution.class, "ResolutionReason");
    }

    /**
     * @return Text field for the description of how this Request was resolved.
     */
    public String getResolutionDetails() {
        return (String) get("Resolution");
    }

    /**
     * @param value Text field for the description of how this Request was
     *                resolved.
     */
    public void setResolutionDetails(String value) {
        set("Resolution", value);
    }

    /**
     * Stories and Defects associated with this Request.
     *
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return Collection of Stories and Defects.
     */
    public Collection<PrimaryWorkitem> getPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        filter.requests.clear();
        filter.requests.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * @return Issues associated with this Request.
     */
    public Collection<Issue> getIssues() {
        return getMultiRelation("Issues");
    }

    /**
     * Gets Epics associated with this Request.
     *
     * @param filter Criteria to filter Epics on.
     * @return selected Epics.
     */
    public Collection<Epic> getEpics(EpicFilter filter) {
        filter = (filter != null) ? filter : new EpicFilter();

        filter.requests.clear();
        filter.requests.add(this);
        return getInstance().get().epics(filter);
    }

    /**
     * Inactivates the Request.
     *
     * @throws UnsupportedOperationException The Request is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Request.
     *
     * @throws UnsupportedOperationException The Request is an invalid state for
     *                 the Operation, e.g. it is already active.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }

    /**
     * Creates a Story from this Request.
     *
     * @return A Story in the VersionOne system related to this Issue.
     */
    public Story generateStory() {
        return generateStory(null);
    }

    /**
     * Creates a Story from this Request.
     *
     * @param attributes additional attributes for the Story.
     * @return A Story in the VersionOne system related to this Issue.
     */
    public Story generateStory(Map<String, Object> attributes) {
        Story result = getInstance().createNew(Story.class, this);
        getInstance().create().addAttributes(result, attributes);
        result.save();
        return result;
    }

    /**
     * Creates a Defect from this Request.
     *
     * @return A Defect in the VersionOne system related to this Issue.
     */
    public Defect generateDefect() {
        return generateDefect(null);
    }

    /**
     * Creates a Defect from this Request.
     * @param attributes additional attributes for the Defect.
     * @return A Defect in the VersionOne system related to this Issue.
     */
    public Defect generateDefect(Map<String, Object> attributes) {
        Defect result = getInstance().createNew(Defect.class, this);
        getInstance().create().addAttributes(result, attributes);
        result.save();
        return result;
    }
}
