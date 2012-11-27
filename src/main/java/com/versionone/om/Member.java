/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;
import java.util.Map;

import com.versionone.om.filters.EffortFilter;
import com.versionone.om.filters.EpicFilter;
import com.versionone.om.filters.IssueFilter;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.filters.RequestFilter;
import com.versionone.om.filters.SecondaryWorkitemFilter;
import com.versionone.om.filters.ThemeFilter;
import com.versionone.om.filters.WorkitemFilter;

/**
 * Represents a user or member in the VersionOne system.
 */
@MetaDataAttribute("Member")
public class Member extends BaseAsset {

    private static final String NICKNAME_VALUE = "Nickname";
    private static final String DEFAULT_ROLE_VALUE = "DefaultRole";

    /**
     * Constructor used to represent an Member entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Member(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Member entity that does NOT yet exist in
     * the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    Member(V1Instance instance) {
        super(instance);
    }

    /**
     * @return The short or abbreviated name of the user or member. This name is
     *         often used in the owner's dropdown.
     */
    public String getShortName() {
        return (String) get(NICKNAME_VALUE);
    }

    /**
     * @param shortName The short or abbreviated name of the user or member.
     *                This name is often used in the owner's dropdown.
     */
    public void setShortName(String shortName) {
        set(NICKNAME_VALUE, shortName);
    }

    /**
     * @return The default role of the user or member. Determines the member's
     *         permissions when assigned to a project. Also determines the
     *         member's global privileges.
     */
    public Role getDefaultRole() {
        return (Role) getRelation(Role.class, DEFAULT_ROLE_VALUE);
    }

    /**
     * @param defaultRole The default role of the user or member. Determines the
     *                member's permissions when assigned to a project. Also
     *                determines the member's global privileges.
     */
    public void setDefaultRole(Role defaultRole) {
        setRelation(DEFAULT_ROLE_VALUE, defaultRole);
    }

    /**
     * @return The username this user or member uses to login to the VersionOne
     *         system.
     */
    public String getUsername() {
        return (String) get("Username");
    }

    /**
     * @param value The username this user or member uses to login to the
     *                VersionOne system.
     */
    public void setUsername(String value) {
        set("Username", value);
    }

    /**
     * @param value The password this user or member uses to login to the
     *                VersionOne system. This is a write-only property.
     */
    public void setPassword(String value) {
        set("Password", value);
    }

    /**
     * @return The Email for this member.
     */
    public String getEmail() {
        return (String) get("Email");
    }

    /**
     * @param value The Email for this member.
     */
    public void setEmail(String value) {
        set("Email", value);
    }

    /**
     * @return The phone number for this member.
     */
    public String getPhone() {
        return (String) get("Phone");
    }

    /**
     * @param value The phone number for this member.
     */
    public void setPhone(String value) {
        set("Phone", value);
    }

    /**
     * Gets Projects this member is assigned to.
     *
     * @return assigned Projects.
     */
    public Collection<Project> getAssignedProjects() {
        return getMultiRelation("Scopes");
    }

    /**
     * @return Conversation messages created by the member.
     */
    public Collection<Conversation> getExpressions() {
    	return getMultiRelation("Expressions");
    }

    /**
     * Gets Stories and Defects owned by this member.
     *
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return selected Workitems.
     */
    public Collection<PrimaryWorkitem> getOwnedPrimaryWorkitems(PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        filter.owners.clear();
        filter.owners.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * Tasks and Tests owned by this member.
     *
     * @param filter Filter for Tasks and Tests.
     * @return Collection of bases of a Tasks or Tests in the VersionOne System.
     */
    public Collection<SecondaryWorkitem> getOwnedSecondaryWorkitems(SecondaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new SecondaryWorkitemFilter();

        filter.owners.clear();
        filter.owners.add(this);
        return getInstance().get().secondaryWorkitems(filter);
    }

    /**
     * A collection of Themes owned by this member.
     *
     * @param filter Limit the themes returned (If null, then all items
     *                returned).
     * @return A collection themes that belong to this member filtered by the
     *         passed in filter.
     */
    public Collection<Theme> getOwnedThemes(ThemeFilter filter) {
        filter = (filter != null) ? filter : new ThemeFilter();

        filter.owners.clear();
        filter.owners.add(this);
        return getInstance().get().themes(filter);
    }

    /**
     * A read-only collection of Issues owned by this member.
     *
     * @param filter Limit the issues returned (If null, then all items
     *                returned).
     * @return A collection issues that belong to this member filtered by the
     *         passed in filter.
     */
    public Collection<Issue> getOwnedIssues(IssueFilter filter) {
        filter = (filter != null) ? filter : new IssueFilter();

        filter.owner.clear();
        filter.owner.add(this);
        return getInstance().get().issues(filter);
    }

    /**
     * A read-only collection of Requests owned by this member.
     *
     * @param filter Limit the requests returned (If null, then all items
     *                returned).
     * @return A collection requests that belong to this member filtered by the
     *         passed in filter.
     */
    public Collection<Request> getOwnedRequests(RequestFilter filter) {
        filter = (filter != null) ? filter : new RequestFilter();

        filter.owner.clear();
        filter.owner.add(this);
        return getInstance().get().requests(filter);
    }

    /**
     * Epics owned by this member.
     *
     * @param filter Limit the epics returned (If null, then all items
     *                returned).
     * @return A collection epics that belong to this member filtered by the
     *         passed in filter.
     */
    public Collection<Epic> getOwnedEpics(EpicFilter filter) {
        filter = (filter != null) ? filter : new EpicFilter();

        filter.owners.clear();
        filter.owners.add(this);
        return getInstance().get().epics(filter);
    }

    /**
     * A collection of Effort Records that belong to this member.
     *
     * @param filter Limit the efforts returned (If null, then all items
     *                returned).
     * @return A collection efforts that belong to this member filtered by the
     *         passed in filter.
     */
    public Collection<Effort> getEffortRecords(EffortFilter filter) {
        filter = (filter != null) ? filter : new EffortFilter();

        filter.member.clear();
        filter.member.add(this);
        return getInstance().get().effortRecords(filter);
    }

    /**
     * Inactivates the Member.
     *
     * @throws UnsupportedOperationException The Member is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Member.
     *
     * @throws UnsupportedOperationException The Member is an invalid state for
     *                 the Operation, e.g. it is already active.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }

    /**
     * Return the total estimate for all stories and defects owned by this
     * member optionally filtered.
     *
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return total estimate of selected Workitems.
     */
    public Double getTotalEstimate(PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        return getSum("OwnedWorkitems:PrimaryWorkitem", filter, "Estimate");
    }

    /**
     * Return the total detail estimate for all workitems owned by this member
     * optionally filtered.
     *
     * @param filter Criteria to filter workitems on.
     * @return total detail estimate of selected Workitems.
     */
    public Double getTotalDetailEstimate(WorkitemFilter filter) {
        filter = (filter != null) ? filter : new WorkitemFilter();

        return getSum("OwnedWorkitems", filter, "DetailEstimate");
    }

    /**
     * Return the total to do for all workitems owned by this member optionally
     * filtered.
     *
     * @param filter Criteria to filter workitems on.
     * @return total to do of selected Workitems.
     */
    public Double getTotalToDo(WorkitemFilter filter) {
        filter = (filter != null) ? filter : new WorkitemFilter();

        return getSum("OwnedWorkitems", filter, "ToDo");
    }

    /**
     * Return the total done for all workitems owned by this member optionally
     * filtered.
     *
     * @param filter Criteria to filter workitems on.
     * @return total done of selected Workitems.
     */
    public Double getTotalDone(WorkitemFilter filter) {
        filter = (filter != null) ? filter : new WorkitemFilter();

        return getSum("OwnedWorkitems", filter, "Actuals.Value");
    }

    /**
     * Creates conversation which mentioned this member.
     *
     * @param author Author of conversation.
     * @param content Content of conversation.
     * @return Created conversation.
     */
    @Override
    public Conversation createConversation(Member author, String content) {
        Conversation conversation = getInstance().create().conversation(author, content);
        conversation.getMentions().add(this);
        conversation.save();
        return conversation;
    }

    /**
     * Creates conversation which mentioned this asset.
     * @param author Author of conversation.
     * @param content Content of conversation.
     * @param attributes additional attributes for the Conversation.
     * @return Created conversation
     */
    @Override
    public Conversation createConversation(Member author, String content, Map<String, Object> attributes) {
        Conversation conversation = getInstance().create().conversation(author, content, attributes);
        conversation.getMentions().add(this);
        conversation.save();
        return conversation;
    }

    /**
     * Creates conversation with this member as author.
     *
     * @param content Content of conversation.
     * @return Created conversation.
     */
    public Conversation createConversation(String content) {
        Conversation conversation = getInstance().create().conversation(this, content);
        return conversation;
    }

	/**
	 * A flag indicating whether this member desires to receive e-mail notifications.
	 */
	public boolean getNotifyViaEmail(){
        return (Boolean) get("NotifyViaEmail");
    }

    public void setNotifyViaEmail(boolean value) {
        set("NotifyViaEmail", value);
    }

    /**
     * Get conversation email notifications status.
     */
    public boolean getSendConversationEmails() {
        return (Boolean) get("SendConversationEmails");
    }

    /**
     * Turn conversation notifications by email on or off.
     */
    public void setSendConversationEmails(boolean status) {
        set("SendConversationEmails", status);
    }
}