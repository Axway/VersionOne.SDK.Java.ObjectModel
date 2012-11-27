package com.versionone.om;

import com.versionone.om.listvalue.TestType;

import java.util.Collection;

/**
 * Regression Test representation.
 */
@MetaDataAttribute("RegressionTest")
public class RegressionTest extends ProjectAsset {

    public RegressionTest(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    public RegressionTest(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Source Test used to generate current Regression Test.
     */
    public Test getGeneratedFrom() {
        return getRelation(Test.class, "GeneratedFrom");
    }

    /**
     * @param generatedFrom Source Test used to generate current Regression Test.
     */
    public void setGeneratedFrom(Test generatedFrom) {
        setRelation("GeneratedFrom", generatedFrom);
    }

    /**
     * Gets Tests generated from current Regression Test.
     *
     * @return Tests generated from current Regression Test.
     */
    public Collection<Test> getGeneratedTests() {
        return getMultiRelation("GeneratedTests");
    }

    /**
     * @return Tags defined for current Regression Test.
     */
    public String getTags() {
        return (String) get("Tags");
    }

    /**
     * @param value Tags defined for current Regression Test.
     */
    public void setTags(String value) {
        set("Tags", value);
    }

    /**
     * @return Related Regression Suites.
     */
    public Collection<RegressionSuite> getRegressionSuites() {
        return getMultiRelation("RegressionSuites");
    }

    /**
     * @return Regression Test owners.
     */
    public Collection<Member> getOwners() {
        return getMultiRelation("Owners");
    }

    /**
     * @return Regression Test status.
     */
    public IListValueProperty getStatus() {
        return getListValue(RegressionTestStatus.class, "Status");
    }

    /**
     * @return Test category.
     */
    @MetaRenamedAttribute("Category")
    public IListValueProperty getType() {
        return getListValue(TestType.class, "Category");
    }

    /**
     * @return Reference value.
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value Reference value.
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * @return Results that we expect when running the test.
     */
    public String getExpectedResults() {
        return (String) get("ExpectedResults");
    }

    /**
     * @param expectedResults Results that we expect when running the test.
     */
    public void setExpectedResults(String expectedResults) {
        set("ExpectedResults", expectedResults);
    }

    /**
     * @return Regression Test steps description.
     */
    public String getSteps() {
        return (String) get("Steps");
    }

    /**
     * @param steps Regression Test steps description.
     */
    public void setSteps(String steps) {
        set("Steps", steps);
    }

    /**
     * @return Test Inputs description.
     */
    public String getInputs() {
        return (String) get("Inputs");
    }

    /**
     * @param inputs Test Inputs description.
     */
    public void setInputs(String inputs) {
        set("Inputs", inputs);
    }

    /**
     * @return Test Setup description.
     */
    public String getSetup() {
        return (String) get("Setup");
    }

    /**
     * @param setup Test Setup description.
     */
    public void setSetup(String setup) {
        set("Setup", setup);
    }

    @Override
    void closeImpl() {
        getInstance().executeOperation(RegressionTest.class, this, "Inactivate");
    }

    @Override
    void reactivateImpl() {
        getInstance().executeOperation(RegressionTest.class, this, "Reactivate");
    }
}
