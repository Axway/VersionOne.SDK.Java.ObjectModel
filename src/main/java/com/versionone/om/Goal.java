/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;

import com.versionone.om.filters.EpicFilter;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.filters.ProjectFilter;
import com.versionone.om.filters.ThemeFilter;
import com.versionone.om.listvalue.GoalPriority;
import com.versionone.om.listvalue.GoalType;

/**
 * Represents a Goal in the VersionOne system.
 */
@MetaDataAttribute("Goal")
public class Goal extends ProjectAsset {

    Goal(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    Goal(V1Instance instance) {
        super(instance);
    }

    /**
     * @return This Goal's Type.
     */
    public IListValueProperty getType() {
        return getListValue(GoalType.class, "Category");
    }

    /**
     * @return This Goal's Priority.
     */
    public IListValueProperty getPriority() {
        return getListValue(GoalPriority.class, "Priority");
    }

    /**
     * A collection of Projects Targeted by this Goal.
     *
     * @param filter Limit the project returned (If null, then all items
     *                returned).
     * @return A collection projects that belong to this goal filtered by the
     *         passed in filter.
     */
    public Collection<Project> getTargetedBy(ProjectFilter filter) {
        filter = (filter != null) ? filter : new ProjectFilter();

        filter.targets.clear();
        filter.targets.add(this);
        return getInstance().get().projects(filter);
    }

    /**
     * Epics assigned to this Goal.
     *
     * @param filter Limit the epic returned (If null, then all items returned).
     * @return A collection epics that belong to this goal filtered by the
     *         passed in filter.
     */
    public Collection<Epic> getEpics(EpicFilter filter) {
        filter = (filter != null) ? filter : new EpicFilter();

        filter.goals.clear();
        filter.goals.add(this);
        return getInstance().get().epics(filter);
    }

    /**
     * Themes assigned to this Goal.
     *
     * @param filter Limit the theme returned (If null, then all items
     *                returned).
     * @return A collection themes that belong to this goal filtered by the
     *         passed in filter.
     */
    public Collection<Theme> getThemes(ThemeFilter filter) {
        filter = (filter != null) ? filter : new ThemeFilter();

        filter.goals.clear();
        filter.goals.add(this);
        return getInstance().get().themes(filter);
    }

    /**
     * Stories and Defects assigned to this Goal.
     *
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return A collection primary work item that belong to this goal filtered
     *         by the passed in filter.
     */
    public Collection<PrimaryWorkitem> getPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        filter.goals.clear();
        filter.goals.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * Inactivates the Goal.
     *
     * @throws UnsupportedOperationException if the Goal is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Goal.
     *
     * @throws UnsupportedOperationException if the Goal is an invalid state for
     *                 the Operation, e.g. it is already active.
     */
    @Override
    void reactivateImpl() {
        getInstance().executeOperation(this, "Reactivate");
    }
}
