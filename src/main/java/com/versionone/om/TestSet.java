/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

@MetaDataAttribute("TestSet")
public class TestSet extends PrimaryWorkitem {
    /**
     * Constructor used to represent an TestSet entity that DOES exist in the VersionOne System.
     *
     * @param assetId  Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    TestSet(AssetID assetId, V1Instance instance) {
        super(assetId, instance);
    }

    /**
     * Constructor used to represent an TestSet entity that does NOT yet exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    TestSet(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Corresponding Regression Suite.
     */
    public RegressionSuite getRegressionSuite() {
        return getRelation(RegressionSuite.class, "RegressionSuite");
    }

    /**
     * @param value Corresponding Regression Suite.
     */
    public void setRegressionSuite(RegressionSuite value) {
        setRelation("RegressionSuite", value);
    }

    /**
     * @return Environment for this Test Set.
     */
    public Environment getEnvironment() {
        return getRelation(Environment.class, "Environment");
    }

    /**
     * @param value Environment for this Test Set.
     */
    public void setEnvironment(Environment value) {
        setRelation("Environment", value);
    }

    /**
     * @return whether acceptance tests can be copied from related Regression Suite.
     */
    public boolean canCopyAcceptanceTestsFromRegressionSuite() {
        return getInstance().canExecuteOperation(this, "CopyAcceptanceTestsFromRegressionSuite");
    }

    /**
     * Copies tests from related Regression Suite.
     */
    public void copyAcceptanceTestsFromRegressionSuite() {
        getInstance().executeOperation(this, "CopyAcceptanceTestsFromRegressionSuite");
    }

    /**
     * Inactivates the Test Set.
     *
     * @throws UnsupportedOperationException Test Set is an invalid state for
     *                                       the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Test Set.
     *
     * @throws UnsupportedOperationException Test Set is an invalid state for
     *                                       the Operation, e.g. it is already active.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }
}
