/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;

import com.versionone.om.listvalue.StoryType;
import com.versionone.om.listvalue.WorkitemRisk;

/**
 * A story or backlog item.
 */
@MetaDataAttribute(value = "Story", defaultOrderByToken = "Story.Order")
public class Story extends PrimaryWorkitem {

    Story(V1Instance instance) {
        super(instance);
    }

    /**
     * Constructor used to represent an Story entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Story(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * @return Name of person or organization requesting this Story.
     */
    public String getRequestedBy() {
        return (String) get("RequestedBy");
    }

    /**
     * @param requestedBy Name of person or organization requesting this Story.
     */
    public void setRequestedBy(String requestedBy) {
        set("RequestedBy", requestedBy);
    }

    /**
     * @return Build number associated with this Story.
     */
    @MetaRenamedAttribute("LastVersion")
    public String getBuild() {
        return (String) get("LastVersion");
    }

    /**
     * @param build Build number associated with this Story.
     */
    public void setBuild(String build) {
        set("LastVersion", build);
    }

    /**
     * @return The Epic that this Story belongs to.
     */
    @MetaRenamedAttribute("Super")
    public Epic getEpic() {
        return getRelation(Epic.class, "Super");
    }

    /**
     * @param epic The Epic that this Story belongs to.
     */
    public void setEpic(Epic epic) {
        setRelation("Super", epic);
    }

    /**
     * @return This Story's Risk.
     */
    public IListValueProperty getRisk() {
        return getListValue(WorkitemRisk.class, "Risk");
    }

    /**
     * @return This Story's Type.
     */
    @MetaRenamedAttribute("Category")
    public IListValueProperty getType() {
        return getListValue(StoryType.class, "Category");
    }

    /**
     * @return Member assigned as a customer for this Story.
     */
    public Member getCustomer() {
        return getRelation(Member.class, "Customer");
    }

    /**
     * @param customer Member assigned as a customer for this Story.
     */
    public void setCustomer(Member customer) {
        setRelation("Customer", customer);
    }

    /**
     * @return Member assigned as a customer for this Story.
     */
    public Collection<Story> getDependsOnStories() {
        return getMultiRelation("Dependencies");
    }

    /**
     * @return Stories that depend on this Story.
     */
    public Collection<Story> getDependentStories() {
        return getMultiRelation("Dependants");
    }

    /**
     * @return The (optional) Retrospective this Story was Identified in.
     */
    public Retrospective getIdentifiedIn() {
        return getRelation(Retrospective.class, "IdentifiedIn");
    }

    /**
     * @param retrospective The (optional) Retrospective this Story was
     *                Identified in.
     */
    public void setIdentifiedIn(Retrospective retrospective) {
        setRelation("IdentifiedIn", retrospective);
    }

    /**
     * Inactivates the Story.
     *
     * @throws UnsupportedOperationException The Story is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Story.
     *
     * @throws UnsupportedOperationException The Story is an invalid state for
     *                 the Operation, e.g. it is already active.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }

    /**
     * Breakdown this Story to Epic
     */
    public void breakdown() {
    	save();
    	getInstance().executeOperation(this, "Breakdown");
    }

    /**
     * Can Story be converted to Epic.
     */
    public boolean canBreakdown() {
        return (Boolean)get("CheckBreakdown");
    }
}
