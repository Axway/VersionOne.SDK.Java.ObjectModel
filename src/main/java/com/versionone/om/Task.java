/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.listvalue.TaskSource;
import com.versionone.om.listvalue.TaskStatus;
import com.versionone.om.listvalue.TaskType;

/**
 * Represents a Task in the VersionOne system.
 */
@MetaDataAttribute(value = "Task", defaultOrderByToken = "Task.Order")
public class Task extends SecondaryWorkitem {

    Task(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    Task(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Build number associated with this task.
     */
    @MetaRenamedAttribute("LastVersion")
    public String getBuild() {
        return (String) get("LastVersion");
    }

    /**
     * @param value Build number associated with this task.
     */
    @MetaRenamedAttribute("LastVersion")
    public void setBuild(String value) {
        set("LastVersion", value);
    }

    /**
     * @return The Source of this Task.
     */
    public IListValueProperty getSource() {
        return getListValue(TaskSource.class, "Source");
    }

    /**
     * @return The Type of this Task.
     */
    @MetaRenamedAttribute("Category")
    public IListValueProperty getType() {
        return getListValue(TaskType.class, "Category");
    }

    /**
     * @return The Status of this Task.
     */
    public IListValueProperty getStatus() {
        return getListValue(TaskStatus.class, "Status");
    }

    /**
     * @return This item's order.
     */
    @MetaRenamedAttribute("Order")
    public Rank<Task> getRankOrder() {
        return (Rank<Task>) getRank("Order");
    }

    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }
}
