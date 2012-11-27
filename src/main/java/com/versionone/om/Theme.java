/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;
import java.util.Map;

import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.filters.ThemeFilter;
import com.versionone.om.filters.WorkitemFilter;
import com.versionone.om.listvalue.ThemeSource;
import com.versionone.om.listvalue.ThemeStatus;
import com.versionone.om.listvalue.ThemeType;
import com.versionone.om.listvalue.WorkitemPriority;
import com.versionone.om.listvalue.WorkitemRisk;

/**
 * Represents a Theme in the VersionOne system.
 */
@MetaDataAttribute("Theme")
public class Theme extends ProjectAsset {

    Theme(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    Theme(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Members that own this item.
     */
    public Collection<Member> getOwners() {
        return getMultiRelation("Owners");
    }

    /**
     * @return This Theme's Risk.
     */
    public IListValueProperty getRisk() {
        return getListValue(WorkitemRisk.class, "Risk");
    }

    /**
     * @return This Theme's Priority.
     */
    public IListValueProperty getPriority() {
        return getListValue(WorkitemPriority.class, "Priority");
    }

    /**
     * @return This Theme's Source.
     */
    public IListValueProperty getSource() {
        return getListValue(ThemeSource.class, "Source");
    }

    /**
     * @return This Theme's Type.
     */
    @MetaRenamedAttribute("Category")
    public IListValueProperty getType() {
        return getListValue(ThemeType.class, "Category");
    }

    /**
     * @return This Theme's Status.
     */
    public IListValueProperty getStatus() {
        return getListValue(ThemeStatus.class, "Status");
    }

    /**
     * @return The Theme this Theme is assigned to.
     */
    @MetaRenamedAttribute("Parent")
    public Theme getParentTheme() {
        return getRelation(Theme.class, "Parent");
    }

    /**
     * @param value The Theme this Theme is assigned to.
     */
    @MetaRenamedAttribute("Parent")
    public void setParentTheme(Theme value) {
        setRelation("Parent", value);
    }

    /**
     * @return Member assigned as a customer for this Theme.
     */
    public Member getCustomer() {
        return getRelation(Member.class, "Customer");
    }

    /**
     * @param value Member assigned as a customer for this Theme.
     */
    public void setCustomer(Member value) {
        setRelation("Customer", value);
    }

    /**
     * @return Build number associated with this Theme.
     */
    @MetaRenamedAttribute("LastVersion")
    public String getBuild() {
        return (String) get("LastVersion");
    }

    /**
     * @param value Build number associated with this Theme.
     */
    @MetaRenamedAttribute("LastVersion")
    public void setBuild(String value) {
        set("LastVersion", value);
    }

    /**
     * @return High-level estimate of this Theme.
     */
    public Double getEstimate() {
        return (Double) get("Estimate");
    }

    /**
     * @param value High-level estimate of this Theme.
     */
    public void setEstimate(Double value) {
        set("Estimate", value);
    }

    /**
     * Stories and Defects assigned to this Theme.
     *
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return A collection primary work items that belong to this theme
     *         filtered by the passed in filter.
     */
    public Collection<PrimaryWorkitem> getPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        filter.theme.clear();
        filter.theme.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * Child Themes of this Theme.
     *
     * @param filter Criteria to filter children of themes. Pass a ThemeFilter
     *                to get only children of themes, respectively.
     * @return A collection children of theme that belong to this theme filtered
     *         by the passed in filter.
     */
    public Collection<Theme> getChildThemes(ThemeFilter filter) {
        filter = (filter != null) ? filter : new ThemeFilter();

        filter.parent.clear();
        filter.parent.add(this);
        return getInstance().get().themes(filter);
    }

    /**
     * Gets the estimate of total effort required to implement this Feature Group.
     *
     * @param filter filter for WorkitemFilter (If null, then all items returned).
     * @return estimate of total effort required to implement this Feature Group.
     */
    public Double getTotalDetailEstimate(WorkitemFilter filter) {
        return getSum("ChildrenMeAndDown", (filter != null) ? filter
                : new WorkitemFilter(), "DetailEstimate");
    }

    /**
     * Gets the total remaining effort required to complete implementation of this Feature Group.
     *
     * @param filter restriction for the work items which have to be counted. If
     *                null, then all items counted.
     * @return total remaining effort required to complete implementation of this Feature Group.
     */
    public Double getTotalToDo(WorkitemFilter filter) {
        return getSum("ChildrenMeAndDown", (filter != null) ? filter
                : new WorkitemFilter(), "ToDo");
    }

    /**
     * @return Goals this Theme is assigned to.
     */
    public Collection<Goal> getGoals() {
        return getMultiRelation("Goals");
    }

    /**
     * Inactivates the Theme.
     *
     * @throws UnsupportedOperationException The Theme is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Theme.
     *
     * @throws UnsupportedOperationException The Theme is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }

    /**
     * Create a theme that is a child of this theme.
     *
     * @param name Name of the new theme.
     * @return The new theme.
     */
    public Theme createChildTheme(String name) {
        Theme theme = getInstance().create().theme(name, getProject());

        theme.setParentTheme(this);
        theme.save();
        return theme;
    }

    /**
     * Create a theme that is a child of this theme.
     *
     * @param name Name of the new theme.
     * @param attributes additional attributes for new theme.
     * @return The new theme.
     */
    public Theme createChildTheme(String name, Map<String, Object> attributes) {
        Theme theme = getInstance().create().theme(name, getProject(), attributes);

        theme.setParentTheme(this);
        theme.save();
        return theme;
    }
}
