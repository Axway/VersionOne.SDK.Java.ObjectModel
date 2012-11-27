package com.versionone.om.filters;

import com.versionone.om.Entity;
import com.versionone.om.Project;
import com.versionone.om.TestSet;
import com.versionone.om.Environment;

import java.util.List;

public class EnvironmentFilter extends EntityFilter {
    @Override
    public Class<? extends Entity> getEntityType() {
        return Environment.class;
    }

    /**
     * Name of this item.
     */
    public final List<String> name = newList();

    /**
     * Project this item belongs to.
     */
    public final List<Project> project = newList();

    /**
     * DisplayID of this item.
     */
    public final List<String> displayID = newList();

    /**
     * TestSets of this item.
     */
    public final List<TestSet> testSet = newList();

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.relation("Scope", project);
        builder.multiRelation("TestSets", testSet);
        builder.simple("Number", displayID);
        builder.simple("Name", name);
    }
}
