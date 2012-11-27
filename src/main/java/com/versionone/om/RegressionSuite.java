/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.filters.TestSetFilter;

import java.util.Collection;
import java.util.Map;

/**
 * The RegressionSuite asset.
 */
@MetaDataAttribute("RegressionSuite")
public class RegressionSuite extends BaseAsset {

    /**
     * Constructor used to represent an RegressionSuite entity that DOES exist in the VersionOne System.
     *
     * @param assetId  Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    RegressionSuite(AssetID assetId, V1Instance instance) {
        super(assetId, instance);
    }

    /**
     * Constructor used to represent an RegressionSuite entity that does NOT yet exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    RegressionSuite(V1Instance instance) {
        super(instance);
    }

    /**
     * @return ID (or Number) of this entity as displayed in the VersionOne system.
     */
    @MetaRenamedAttribute("Number")
    public String getDisplayID() {
        return (String) get("Number");
    }

    /**
     * @return The Member who owns this RegressionSuite.
     */
    public Member getOwner() {
        return getRelation(Member.class, "Owner");
    }

    /**
     * @param value The Member who owns this RegressionSuite.
     */
    public void setOwner(Member value) {
        setRelation("Owner", value);
    }

    /**
     * @return High-level estimate (in story points) of this item.
     */
    public Double getEstimate() {
        return (Double) get("Estimate");
    }

    /**
     * @param value High-level estimate (in story points) of this item.
     */
    public void setEstimate(Double value) {
        set("Estimate", value);
    }

    /**
     * @return Cross-reference of this RegressionSuite with an external system.
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value Cross-reference of this RegressionSuite with an external system.
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * @return Regression Plan associated with this suite.
     */
    public RegressionPlan getRegressionPlan() {
        return getRelation(RegressionPlan.class, "RegressionPlan");
    }

    /**
     * @param value Regression Plan associated with this suite.
     */
    public void setRegressionPlan(RegressionPlan value) {
        setRelation("RegressionPlan", value);
    }

    /**
     * @return Regression Tests associated with this suite.
     */
    public Collection<RegressionTest> getRegressionTests() {
        return getMultiRelation("RegressionTests");
    }

    /**
     * Assign Regression Test to this suite.
     *
     * @param regressionTest Regression Test to assign.
     */
    public void assignRegressionTest(RegressionTest regressionTest) {
        getRegressionTests().add(regressionTest);
    }

    /**
     * Un-assign Regression Test from this suite.
     *
     * @param regressionTest Regression Test to un-assign.
     */
    public void unassignRegressionTest(RegressionTest regressionTest) {
        if (!getRegressionTests().contains(regressionTest)) {
            throw new IllegalArgumentException("Suite doesn't have this regression test.");
        }
        getRegressionTests().remove(regressionTest);
    }

    /**
     * Creates new TestSet
     *
     * @param name Test Set name
     * @return Created entity
     */
    public TestSet createTestSet(String name) {
        return getInstance().create().testSet(name, this, getRegressionPlan().getProject());
    }

    /**
     * Creates new TestSet.
     *
     * @param name       Test Set name
     * @param attributes Additional attributes for the brand new Test Set
     * @return Created entity
     */
    public TestSet createTestSet(String name, Map<String, Object> attributes) {
        return getInstance().create().testSet(name, this, getRegressionPlan().getProject(), attributes);
    }

    /**
     * Request related Test Sets.
     *
     * @param filter Test Set filter
     * @return Related Test Sets
     */
    public Collection<TestSet> getTestSets(TestSetFilter filter) {
        filter = (filter != null) ? filter : new TestSetFilter();

        filter.regressionSuite.clear();
        filter.regressionSuite.add(this);
        return getInstance().get().testSets(filter);
    }

    @Override
    boolean canCloseImpl() {
        return false;
    }

    @Override
    boolean canReactivateImpl() {
        return false;
    }

    /**
     * @throws UnsupportedOperationException The RegressionSuite is an invalid state for
     *                                       the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot close regression suite.");
    }

    /**
     * @throws UnsupportedOperationException The RegressionSuite is an invalid state for
     *                                       the Operation, e.g. it is already active.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot reactivate regression suite.");
    }
}
