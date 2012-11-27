/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.listvalue.TestStatus;
import com.versionone.om.listvalue.TestType;

import java.util.Collection;

/**
 * Represents a Test in the VersionOne System.
 */
@MetaDataAttribute(value = "Test", defaultOrderByToken = "Test.Order")
public class Test extends SecondaryWorkitem {

    Test(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    Test(V1Instance instance) {
        super(instance);
    }

    /**
     * @return The Type of this Test.
     */
    @MetaRenamedAttribute("Category")
    public IListValueProperty getType() {
        return getListValue(TestType.class, "Category");
    }

    /**
     * @return The Status of this Test.
     */
    public IListValueProperty getStatus() {
        return getListValue(TestStatus.class, "Status");
    }

    /**
     * @return This item's order.
     */
    @MetaRenamedAttribute("Order")
    public Rank<Test> getRankOrder() {
        return (Rank<Test>) getRank("Order");
    }

    /**
     * @return collection of Regression Tests generated from Test.
     */
    public Collection<RegressionTest> getGeneratedRegressionTests() {
        return getMultiRelation("GeneratedRegressionTests");
    }

    /**
     * @return Regression Test from with Test was generated
     */
    public RegressionTest getGeneratedFrom() {
        return getRelation(RegressionTest.class, "GeneratedFrom");
    }

    /**
     * @param generatedFrom Regression Test from with Test was generated
     */
    public void setGeneratedFrom(RegressionTest generatedFrom) {
        setRelation("GeneratedFrom", generatedFrom);
    }

    /**
     * Generates Regression Test base on the current test.
     *
     * @return Generated Regression Test
     */
    public RegressionTest generateRegressionTest() {
        return getInstance().create().regressionTest(this);
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