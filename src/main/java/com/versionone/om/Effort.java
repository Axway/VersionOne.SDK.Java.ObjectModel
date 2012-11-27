/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.DB.DateTime;

/**
 * Represents an Effort Record in the VersionOne system.
 */
@MetaDataAttribute("Actual")
public class Effort extends Entity {

    /**
     * 'Date' attribute.
     */
    public static final String ATTRIBUTE_DATE = "Date";

    /**
     *  'Value' attribute.
     */
    public static final String ATTRIBUTE_VALUE = "Value";

    /**
     * Constructor used to represent an Effort that DOES exist in the VersionOne
     * System.
     *
     * @param id Unique ID of this Effort.
     * @param instance this Effort belongs to.
     */
    public Effort(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Effort that does NOT exist yet in the
     * VersionOne System.
     *
     * @param instance this Effort belongs to.
     */
    Effort(V1Instance instance) {
        super(instance);
    }

    /**
     * Gets Story, Defect, Task, Test that this Effort record belongs to.
     *
     * @return belonged Entity.
     */
    public Workitem getWorkitem() {
        return getRelation(Workitem.class, "Workitem");
    }

    /**
     * Sets Story, Defect, Task, Test that this Effort record belongs to.
     *
     * @param workitem belonged Entity.
     */
    public void setWorkitem(Workitem workitem) {
        setRelation("Workitem", workitem);
    }

    /**
     * Gets the Project this effort record belongs to.
     *
     * @return belonged Project.
     */
    @MetaRenamedAttribute("Scope")
    public Project getProject() {
        return getRelation(Project.class, "Scope");
    }

    /**
     * Sets the Project this effort record belongs to.
     *
     * @param project belonged Project.
     */
    public void setProject(Project project) {
        setRelation("Scope", project);
    }

    /**
     * Gets Member this effort record belongs to.
     *
     * @return belonged Member.
     */
    public Member getMember() {
        return getRelation(Member.class, "Member");
    }

    /**
     * Sets Member this effort record belongs to.
     *
     * @param member belonged Member.
     */
    public void setMember(Member member) {
        setRelation("Member", member);
    }

    /**
     * Gets Iteration this effort record belongs to.
     *
     * @return belonged Iteration.
     */
    @MetaRenamedAttribute("Timebox")
    public Iteration getIteration() {
        return getRelation(Iteration.class, "Timebox");
    }

    /**
     * Sets Iteration this effort record belongs to.
     *
     * @param iteration belonged Iteration.
     */
    public void setIteration(Iteration iteration) {
        setRelation("Timebox", iteration);
    }

    /**
     * Gets Team this effort record belongs to.
     *
     * @return belonged Team.
     */
    public Team getTeam() {
        return getRelation(Team.class, "Team");
    }

    /**
     * Sets Team this effort record belongs to.
     *
     * @param team belonged Team.
     */
    public void setTeam(Team team) {
        setRelation("Team", team);
    }

    /**
     * Gets Date this effort record is logged against.
     *
     * @return this Effort date.
     */
    public DateTime getDate() {
        return new DateTime(get(ATTRIBUTE_DATE));
    }

    /**
     * Sets Date this effort record is logged against.
     *
     * @param date this Effort date.
     */
    public void setDate(DateTime date) {
        set(ATTRIBUTE_DATE, date.getDate());
    }

    /**
     * Gets Value of this effort record.
     *
     * @return this Effort value.
     */
    public double getValue() {
        return (Double) get(ATTRIBUTE_VALUE);
    }

    /**
     * Sets Value of this effort record.
     *
     * @param value this Effort value.
     */
    public void setValue(double value) {
        set(ATTRIBUTE_VALUE, value);
    }
}
