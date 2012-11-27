/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.filters.TestSetFilter;

import java.util.Collection;

/**
 * The Environment asset.
 */
@MetaDataAttribute("Environment")
public class Environment extends Entity {
    protected static final int STATE_CLOSED = 128;
    protected static final int STATE_ACTIVE = 64;

    /**
     * Constructor used to represent an Environment entity that DOES exist in the VersionOne System.
     *
     * @param id       Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    protected Environment(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Environment entity that does NOT yet exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    protected Environment(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Name of the Environment.
     */
    public String getName() {
        return (String) get(NAME_VALUE);
    }

    /**
     * @param name Name of the Environment.
     */
    public void setName(String name) {
        set(NAME_VALUE, name);
    }

    /**
     * @return The Project this Environment belongs in. This must be present.
     */
    @MetaRenamedAttribute("Scope")
    public Project getProject() {
        return getRelation(Project.class, "Scope");
    }

    /**
     * @param project The Project this Environment belongs in.
     */
    public void setProject(Project project) {
        setRelation("Scope", project);
    }

    /**
     * @return ID (or Number) of this entity as displayed in the VersionOne system.
     */
    @MetaRenamedAttribute("Number")
    public String getDisplayID() {
        return (String) get("Number");
    }

    /**
     * TestSets associated with this Environment.
     *
     * @param filter Criteria to filter TestSets on.
     * @return Related Test Sets.
     */
    public Collection<TestSet> getTestSets(TestSetFilter filter) {
        filter = (filter != null) ? filter : new TestSetFilter();

        filter.environment.clear();
        filter.environment.add(this);

        return getInstance().get().testSets(filter);
    }

    /**
     * @return Indicates if the entity is open or active.
     */
    public boolean isActive() {
        return isActiveImpl();
    }

    /**
     * @return Indicates if the entity is closed or inactive.
     */
    public boolean isClosed() {
        return isClosedImpl();
    }

    /**
     * @return Internal worker function to determine active state.
     */
    boolean isActiveImpl() {
        return get(ASSET_STATE_VALUE, true).equals(STATE_ACTIVE);
    }

    /**
     * @return Internal worker function to determine closed state.
     */
    boolean isClosedImpl() {
        return get(ASSET_STATE_VALUE, true).equals(STATE_CLOSED);
    }

    /**
     * @return True if the item can be closed.
     */
    public boolean canClose() {
        return canCloseImpl();
    }

    boolean canCloseImpl() {
        return getInstance().canExecuteOperation(this, "Inactivate");
    }

    /**
     * Closes or Inactivates the item.
     *
     * @throws UnsupportedOperationException if the item is an invalid state for
     *                                       the Operation, e.g. it is already closed or inactive.
     */
    public void close() throws UnsupportedOperationException {
        save();
        closeImpl();
        clearCache(ASSET_STATE_VALUE);
    }

    /**
     * Inactivates the Environment.
     *
     * @throws UnsupportedOperationException The Environment is an invalid state for
     *                                       the Operation, e.g. it is already closed.
     */
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * @return True if item can be Reactivated.
     */
    public boolean canReactivate() {
        return canReactivateImpl();
    }

    boolean canReactivateImpl() {
        return getInstance().canExecuteOperation(this, "Reactivate");
    }

    /**
     * Reactivates the item.
     *
     * @throws UnsupportedOperationException if the item is an invalid state for
     *                                       the Operation, e.g. it is already open or active.
     */
    public void reactivate() throws UnsupportedOperationException {
        reactivateImpl();
        save();
        clearCache(ASSET_STATE_VALUE);
    }

    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }
}