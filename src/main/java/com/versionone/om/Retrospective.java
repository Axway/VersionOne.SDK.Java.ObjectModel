/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;
import java.util.Map;

import com.versionone.DB.DateTime;
import com.versionone.om.filters.IssueFilter;
import com.versionone.om.filters.StoryFilter;

/**
 * Represents a Retrospective in the VersionOne System
 */
@MetaDataAttribute("Retrospective")
public class Retrospective extends BaseAsset {

    Retrospective(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    Retrospective(V1Instance instance) {
        super(instance);
    }

    @Override
    void closeImpl() {
        getInstance().executeOperation(this, "Inactivate");
    }

    @Override
    void reactivateImpl() {
        getInstance().executeOperation(this, "Reactivate");
    }

    /**
     * @return The Project this ProjectAsset belongs in. This must be present.
     */
    public Project getProject() {
        return getRelation(Project.class, "Scope");
    }

    /**
     * @param value The Project this ProjectAsset belongs in. This must be
     *                present.
     */
    public void setProject(Project value) {
        setRelation("Scope", value);
    }

    /**
     * A read-only collection of Stories Identified in the Retrospective.
     *
     * @param filter filter for getting list of stories.
     * @return collection of Stories Identified in the Retrospective.
     */
    public Collection<Story> getIdentifiedStories(StoryFilter filter) {
        filter = (filter != null) ? filter : new StoryFilter();

        filter.identifiedIn.clear();
        filter.identifiedIn.add(this);
        return getInstance().get().story(filter);
    }

    /**
     * A collection of Issues Identified in the Retrospective.
     *
     * @param filter filter for getting list of issues.
     * @return collection of Issues Identified in the Retrospective.
     */
    public Collection<Issue> getIssues(IssueFilter filter) {
        filter = (filter != null) ? filter : new IssueFilter();

        filter.retrospective.clear();
        filter.retrospective.add(this);
        return getInstance().get().issues(filter);
    }

    /**
     * @return The Retrospective Facilitator.
     */
    public Member getFacilitatedBy() {
        return getRelation(Member.class, "FacilitatedBy");
    }

    /**
     * @param value The Retrospective Facilitator.
     */
    public void setFacilitatedBy(Member value) {
        setRelation("FacilitatedBy", value);
    }

    /**
     * @return The Iteration this Retrospective was conducted for.
     */
    public Iteration getIteration() {
        return getRelation(Iteration.class, "Timebox");
    }

    /**
     * @param value The Iteration this Retrospective was conducted for.
     */
    public void setIteration(Iteration value) {
        setRelation("Timebox", value);
    }

    /**
     * @return The date this Retrospective was conducted.
     */
    public DateTime getDate() {
        return new DateTime(get("Date"));
    }

    /**
     * @param value The date this Retrospective was conducted.
     */
    public void setDate(DateTime value) {
        set("Date", value.getDate());
    }

    /**
     * @return The Team this Retrospecive belongs to.
     */
    public Team getTeam() {
        return getRelation(Team.class, "Team");
    }

    /**
     * @param value The Team this Retrospecive belongs to.
     */
    public void setTeam(Team value) {
        setRelation("Team", value);
    }

    /**
     * @return The Retrospective Summary.
     */
    public String getSummary() {
        return (String) get("Summary");
    }

    /**
     * @param value The Retrospective Summary.
     */
    public void setSummary(String value) {
        set("Summary", value);
    }

    /**
     * Creates a Story related to this Retrospective.
     *
     * @param name The name of the Story.
     * @return The newly saved Story.
     */
    public Story createStory(String name) {
        return getInstance().create().story(name, this);
    }

    /**
     * Creates a Story related to this Retrospective.
     *
     * @param name The name of the Story.
     * @param attributes additional attributes for Story
     * @return The newly saved Story.
     */
    public Story createStory(String name, Map<String, Object> attribute) {
        return getInstance().create().story(name, this, attribute);
    }

    /**
     * Creates an Issue related to this Retrospective.
     *
     * @param name The name of the Issue.
     * @return The newly saved Issue.
     */
    public Issue createIssue(String name) {
        return getInstance().create().issue(name, this);
    }

    /**
     * Creates an Issue related to this Retrospective.
     *
     * @param name The name of the Issue.
     * @param attributes additional attributes for the Issue.
     * @return The newly saved Issue.
     */
    public Issue createIssue(String name, Map<String, Object> attributes) {
        return getInstance().create().issue(name, this, attributes);
    }
}
