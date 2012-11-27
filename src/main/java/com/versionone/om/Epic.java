/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.filters.EpicFilter;
import com.versionone.om.filters.StoryFilter;
import com.versionone.om.filters.TestFilter;
import com.versionone.om.filters.DefectFilter;
import com.versionone.om.filters.TestSetFilter;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.listvalue.*;

import java.util.Collection;

/**
 * Represents an Epic in the VersionOne system.
 */
@MetaDataAttribute(value = "Epic")
public class Epic extends Workitem {

    /**
     * Constructor used to represent an Epic entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Epic(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Epic entity that does NOT yet exist in
     * the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    Epic(V1Instance instance) {
        super(instance);
    }

    /**
     * @return The Parent Epic that this Epic belongs to.
     */
    public Epic getSuper() {
        return getRelation(Epic.class, "Super");
    }

    /**
     * @param value The Parent Epic that this Epic belongs to.
     */
    public void setSuper(Epic value) {
        setRelation("Super", value);
    }

    /**
     * @return The Workitem this Epic is assigned to.
     */
    public Workitem getParent() {
        return getRelation(Workitem.class, "Parent");
    }

    /**
     * @return Swag for the Epic.
     */
    public Double getSwag() {
        return (Double) get("Swag");
    }

    /**
     * @param value Swag for the Epic.
     */
    public void setSwag(Double value) {
        set("Swag", value);
    }

    public Double getValue() {
        return (Double) get("Value");
    }

    public void setValue(Double value) {
        set("Value", value);
    }

    /**
     * @return This Epic's Status.
     */
    public IListValueProperty getStatus() {
        return getListValue(EpicStatus.class, "Status");
    }

    /**
     * @return This Epic's Priority.
     */
    public IListValueProperty getPriority() {
        return getListValue(EpicPriority.class, "Priority");
    }

    /**
     * @return This Epic's Source.
     */
    public IListValueProperty getSource() {
        return getListValue(WorkitemSource.class, "Source");
    }

    /**
     * @return This Epic's Risk.
     */
    public Double getRisk() {
        return (Double) get("Risk");
    }

    /**
     * @param value Risk for the Epic.
     */
    public void setRisk(Double value) {
        set("Risk", value);
    }

    /**
     * @return This Epic's Type.
     */
    @MetaRenamedAttribute("Category")
    public IListValueProperty getType() {
        return getListValue(EpicType.class, "Category");
    }


    /**
     * @return This item's order.
     */
    @MetaRenamedAttribute("Order")
    public Rank<Epic> getRankOrder() {
    	return (Rank<Epic>) getRank("Order");
    }

    /**
     * Epics that are immediate children of this Epic.
     *
     * @param filter Limit the epic returned (If null, then all items returned).
     * @return A collection epics that belong to this epic filtered by the
     *         passed in filter.
     */
    public Collection<Epic> getChildEpics(EpicFilter filter) {
        filter = (filter != null) ? filter : new EpicFilter();
        filter.parent.clear();
        filter.parent.add(this);
        return getInstance().get().epics(filter);
    }

    /**
     * Stories that are immediate children of this Epic.
     *
     * @param filter Limit the story returned (If null, then all items
     *                returned).
     * @return A collection stories that belong to this epic filtered by the
     *         passed in filter.
     */
    public Collection<Story> getChildStories(StoryFilter filter) {
        filter = (filter != null) ? filter : new StoryFilter();
        filter.epic.clear();
        filter.epic.add(this);
        return getInstance().get().story(filter);
    }

    /// <summary>
    ///
    /// </summary>

    /**
     * Tests that are immediate children of this Epic
     * @param filter Limit the tests returned (If null, then all items
     *                returned).
     * @return A collection tests that belong to this epic filtered by the
     *         passed in filter.
     */
    public Collection<Test> getChildTests(TestFilter filter) {
        filter = (filter != null) ? filter : new TestFilter();
        filter.epic.clear();
        filter.epic.add(this);
        return getInstance().get().tests(filter);
    }
    
    
    public Collection<Defect> getChildDefects(DefectFilter filter) {
        filter = (filter != null) ? filter : new DefectFilter();
        filter.epic.clear();
        filter.epic.add(this);
        return getInstance().get().defects(filter);
    }
    
    
    public Collection<TestSet> getChildTestSets(TestSetFilter filter) {
        filter = (filter != null) ? filter : new TestSetFilter();
        filter.epic.clear();
        filter.epic.add(this);
        return getInstance().get().testSets(filter);
    }
    
    
    public Collection<PrimaryWorkitem> getChildPrimaryWorkitems(PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();
        filter.epic.clear();
        filter.epic.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }
    
    
    
    
    
    

    /**
     * @return Members that own this Epic.
     */
    public Collection<Member> getOwners() {
        return getMultiRelation("Owners");
    }

    /**
     * @return Goals this Epic is assigned to.
     */
    public Collection<Goal> getGoals() {
        return getMultiRelation("Goals");
    }

    /**
     * @return Requests associated with this Epic.
     */
    public Collection<Request> getRequests() {
        return getMultiRelation("Requests");
    }

    /**
     * @return Issues associated with this Epic.
     */
    public Collection<Issue> getIssues() {
        return getMultiRelation("Issues");
    }

    /**
     * @return Blocking Issues associated with this Epic.
     */
    public Collection<Issue> getBlockingIssues() {
        return getMultiRelation("BlockingIssues");
    }

    /**
     * @return Name of person or organization requesting this Epic.
     */
    public String getRequestedBy() {
        return (String) get("RequestedBy");
    }

    /**
     * @param value Name of person or organization requesting this Epic.
     */
    public void setRequestedBy(String value) {
        set("RequestedBy", value);
    }

    /**
     * Inactivates the Story.
     *
     * @throws UnsupportedOperationException The Epic is an invalid state for
     *                 the Operation, e.g. it is already closed.
     * @see com.versionone.om.BaseAsset#closeImpl().
     * @see com.versionone.om.BaseAsset#closeImpl().
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Story.
     *
     * @throws UnsupportedOperationException The Epic is an invalid state for
     *                 the Operation, e.g. it is already active.
     * @see com.versionone.om.BaseAsset#reactivateImpl().
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }

    /**
     * @return True if a Story can be generated from this Epic.
     */
    public boolean canGenerateChildStory() {
        return (Boolean) get("CheckGenerateSubStory");
    }

    /**
     * Generates a Story that is the child of this Epic.
     *
     * @return Story that is a child of this Epic.
     * @throws UnsupportedOperationException If the Epic is not able to Generate
     *                 a child Story.
     */
    public Story generateChildStory() throws UnsupportedOperationException {
        save();
        return getInstance().executeOperation(Story.class, this,
                "GenerateSubStory");
    }

    /**
     * @return True if an Epic can be generated from this Epic.
     */
    public boolean canGenerateChildEpic() {
    	return (Boolean) get("CheckGenerateSubEpic");
    }

    /**
     * @return Generates an Epic that is the child of this Epic.
     * @throws UnsupportedOperationException If the Epic is not able to Generate
     *                 a child Epic.
     */
    public Epic generateChildEpic() throws UnsupportedOperationException {
		save();
        return getInstance().executeOperation(Epic.class, this, "GenerateSubEpic");
    }
}
