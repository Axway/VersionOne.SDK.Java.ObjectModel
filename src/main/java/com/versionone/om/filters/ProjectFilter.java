/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Entity;
import com.versionone.om.Goal;
import com.versionone.om.Project;
import com.versionone.om.TestSuite;

/**
 * Filter for getting projects.
 */
public class ProjectFilter extends BaseAssetFilter {
    /**
     * Filter on Targets.
     */
    public List<Goal> targets = newList();

    /**
     * Filter on Parent Project.
     */
    public List<Project> parent = newList();

    /**
     * Filter on Project TestSuite.
     */
    public List<TestSuite> testSuite = newList();

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Project.class;
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);
        builder.relation("Parent", parent);
        builder.relation("TestSuite", testSuite);
        builder.multiRelation("Targets", targets);
    }
}
