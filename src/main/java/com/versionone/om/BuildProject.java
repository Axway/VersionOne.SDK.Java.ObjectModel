/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.DB.DateTime;
import com.versionone.om.filters.BuildRunFilter;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a Build Project in the VersionOne System.
 */
@MetaDataAttribute("BuildProject")
public class BuildProject extends BaseAsset {
    /**
     * Constructor used to represent an BuildProject entity that DOES exist in
     * the VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    BuildProject(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an BuildProject entity that does NOT yet
     * exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    BuildProject(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Reference of this Build Project.
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value Reference of this Build Project.
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * @param filter limit the build runs returned; (If null, then all items
     *                returned).
     * @return A collection of Build Runs associated with this Build Project.
     */
    public Collection<BuildRun> getBuildRuns(BuildRunFilter filter) {
        filter = (filter == null) ? new BuildRunFilter() : filter;

        filter.buildProjects.clear();
        filter.buildProjects.add(this);
        return getInstance().get().buildRuns(filter);
    }

    /**
     * Create a Build Run with the given name and date in this Build Project.
     *
     * @param name of creating BuildRun.
     * @param date of creating BuildRun.
     * @return A new Build Run in this Build Project.
     */
    public BuildRun createBuildRun(String name, DateTime date) {
        return getInstance().create().buildRun(this, name, date);
    }

    /**
     * Create a Build Run with the given name and date in this Build Project.
     *
     * @param name of creating BuildRun.
     * @param date of creating BuildRun.
     * @param attributes additional attributes for the BuildRun.
     * @return A new Build Run in this Build Project.
     */
    public BuildRun createBuildRun(String name, DateTime date, Map<String, Object> attributes) {
        return getInstance().create().buildRun(this, name, date, attributes);
    }

    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(BuildProject.class, this, "Inactivate");
    }

    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(BuildProject.class, this, "Reactivate");
    }
}
