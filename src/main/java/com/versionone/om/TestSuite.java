/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;

import com.versionone.om.filters.ProjectFilter;

/**
 * The TestSuite asset.
 */
@MetaDataAttribute("TestSuite")
public class TestSuite extends BaseAsset {
    TestSuite(V1Instance instance) {
        super(instance);
    }

    TestSuite(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * @return Reference is a free text field used for reference (perhaps to an
     *         external system).
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value Reference is a free text field used for reference (perhaps
     *                to an external system).
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * Projects associated with this TestSuite.
     *
     * @param filter filter for getting list of projects.
     * @return collection of project associated with this test suit.
     */
    public Collection<Project> getProjects(ProjectFilter filter) {
        filter = (filter != null) ? filter : new ProjectFilter();

        filter.testSuite.clear();
        filter.testSuite.add(this);
        return getInstance().get().projects(filter);
    }

    /**
     * Inactivates the TestSuite.
     *
     * @throws UnsupportedOperationException The TestSuite is an invalid state
     *                 for the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the TestSuite.
     *
     * @throws UnsupportedOperationException The Theme is an invalid state for
     *                 the Operation, e.g. it is already closed.
     */
    @Override
    void reactivateImpl() {
        getInstance().executeOperation(this, "Reactivate");
    }
}
