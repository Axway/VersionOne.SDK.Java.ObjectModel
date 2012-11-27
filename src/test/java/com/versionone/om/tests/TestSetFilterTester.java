package com.versionone.om.tests;

import com.versionone.om.Environment;
import com.versionone.om.Project;
import com.versionone.om.RegressionPlan;
import com.versionone.om.RegressionSuite;
import com.versionone.om.TestSet;
import com.versionone.om.filters.TestSetFilter;
import org.junit.*;

import java.util.Collection;

public class TestSetFilterTester extends BaseSDKTester {
    private RegressionPlan regressionPlan;
    private RegressionSuite regressionSuite1;
    private RegressionSuite regressionSuite2;

    private final String TEST_SET_NAME = "test set 1";

    @Before
    public void before() {
        regressionPlan = getEntityFactory().createRegressionPlan("test plan", getSandboxProject());
        regressionSuite1 = getEntityFactory().createRegressionSuite("suite 1", regressionPlan);
        regressionSuite2 = getEntityFactory().createRegressionSuite("suite 2", regressionPlan);
    }

    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Test
    public void getTestSetsWithNullFilterTest() {
        Collection<TestSet> beforeCreation = getInstance().get().testSets(null);
        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite1);
        Collection<TestSet> afterCreation = getInstance().get().testSets(null);

        Assert.assertTrue(afterCreation.size() - beforeCreation.size() == 1);
        ListAssert.notcontains(testSet, beforeCreation);
        ListAssert.contains(testSet, afterCreation);
    }

    @Test
    public void filterByRegressionSuiteTest() {
        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite1);
        TestSetFilter filter = new TestSetFilter();

        filter.regressionSuite.add(regressionSuite2);

        Collection<TestSet> testSets = getInstance().get().testSets(filter);
        Assert.assertEquals(0, testSets.size());

        filter.regressionSuite.add(regressionSuite1);

        testSets = getInstance().get().testSets(filter);
        Assert.assertEquals(1, testSets.size());
        ListAssert.contains(testSet, testSets);

        filter.regressionSuite.remove(regressionSuite2);

        testSets = getInstance().get().testSets(filter);
        Assert.assertEquals(1, testSets.size());
    }

    @Test
    public void filterByEnvironmentTest() {
        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite1);
        Environment environment = createEnvironment("Environment for TestSet", null);
        Environment environment2 = createEnvironment("Environment2 for TestSet", null);
        testSet.setEnvironment(environment);
        testSet.save();

        TestSetFilter filter = new TestSetFilter();

        filter.environment.add(environment2);

        Collection<TestSet> testSets = getInstance().get().testSets(filter);
        Assert.assertEquals(0, testSets.size());

        filter.environment.add(environment);

        testSets = getInstance().get().testSets(filter);
        Assert.assertEquals(1, testSets.size());
        ListAssert.contains(testSet, testSets);

        filter.environment.remove(environment2);

        testSets = getInstance().get().testSets(filter);
        Assert.assertEquals(1, testSets.size());
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }
}
