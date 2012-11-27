package com.versionone.om.tests;

import com.versionone.om.*;
import com.versionone.om.filters.TestFilter;
import com.versionone.om.filters.TestSetFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

public class TestSetTester extends BaseSDKTester {
    private RegressionSuite regressionSuite;

    private final String TEST_SET_NAME = "test set 1";
    private final String TEST_SET_DESCRIPTION = "test set 1 description";
    private final String TEST_SET_DESCRIPTION_UPDATED = "test set 1 description (with slight modifications)";

    @Before
    public void before() {
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan("test plan", getSandboxProject());
        regressionSuite = getEntityFactory().createRegressionSuite("suite 1", regressionPlan);
    }

    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Test
    public void createTestSetTest() {
        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite);
        TestSetFilter filter = new TestSetFilter();
        filter.regressionSuite.add(regressionSuite);
        filter.project.add(getSandboxProject());

        resetInstance();

        List<TestSet> testSets = new ArrayList<TestSet>(getInstance().get().testSets(filter));
        ListAssert.contains(testSet, testSets);
        Assert.assertTrue(testSets.get(0).getProject().equals(getSandboxProject()));
        Assert.assertTrue(testSets.get(0).getRegressionSuite().equals(regressionSuite));
    }

    @Test
    public void createTestSetWithAttributesTest() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", TEST_SET_DESCRIPTION);

        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite, attributes);

        resetInstance();

        TestSetFilter filter = new TestSetFilter();
        filter.regressionSuite.add(regressionSuite);
        filter.project.add(getSandboxProject());

        List<TestSet> testSets = new ArrayList<TestSet>(getInstance().get().testSets(filter));
        ListAssert.contains(testSet, testSets);
        Assert.assertTrue(testSets.get(0).getProject().equals(getSandboxProject()));
        Assert.assertTrue(testSets.get(0).getRegressionSuite().equals(regressionSuite));
    }

    @Test
    public void updateTestSetTest() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", TEST_SET_DESCRIPTION);

        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite, attributes);
        testSet.setDescription(TEST_SET_DESCRIPTION_UPDATED);
        testSet.save();

        resetInstance();

        TestSet queriedTestSet = getInstance().get().testSetByDisplayID(testSet.getDisplayID());
        Assert.assertTrue(queriedTestSet.getDescription().equals(TEST_SET_DESCRIPTION_UPDATED));
    }

    @Test
    public void deleteTestSetTest() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", TEST_SET_DESCRIPTION);

        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite, attributes);

        resetInstance();

        TestSetFilter filter = new TestSetFilter();
        filter.regressionSuite.add(regressionSuite);
        filter.project.add(getSandboxProject());
        Collection<TestSet> testSets = getInstance().get().testSets(filter);

        Assert.assertTrue(testSets.size() == 1);
        testSet.delete();

        resetInstance();

        testSets = getInstance().get().testSets(filter);
        Assert.assertTrue(testSets.size() == 0);
    }

    @Test
    public void getTestSetByIDTest() {
        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite);
        testSet.setDescription(TEST_SET_DESCRIPTION);
        testSet.save();

        resetInstance();

        TestSet queriedTestSet = getInstance().get().testSetByID(testSet.getID());
        Assert.assertEquals(testSet, queriedTestSet);
    }


    @Test
    public void createTestSetWithEnvironmentTest() {
        Environment environment = createEnvironment("Environment for TestSet", null);
        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite);
        testSet.setEnvironment(environment);
        testSet.save();

        TestSetFilter filter = new TestSetFilter();
        filter.environment.add(environment);
        filter.project.add(getSandboxProject());

        resetInstance();

        List<TestSet> testSets = new ArrayList<TestSet>(getInstance().get().testSets(filter));
        ListAssert.contains(testSet, testSets);
        Assert.assertTrue(testSets.get(0).getProject().equals(getSandboxProject()));
        Assert.assertTrue(testSets.get(0).getEnvironment().equals(environment));
    }

    @Test
    public void environmentTest() {
        Environment env = getEnvironment();

        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite);
        resetInstance();

        TestSet newTestSet = getInstance().get().testSetByID(testSet.getID());
        Assert.assertNull(newTestSet.getEnvironment());

        testSet.setEnvironment(env);
        testSet.save();

        resetInstance();
        newTestSet = getInstance().get().testSetByID(testSet.getID());

        Assert.assertNotNull(env);
        Assert.assertEquals(env, newTestSet.getEnvironment());
    }

    @Test
    public void canReactivateTest() {
        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite);

        Assert.assertFalse(testSet.canReactivate());
        Assert.assertTrue(testSet.isActive());

        testSet.close();

        Assert.assertTrue(testSet.canReactivate());
        Assert.assertFalse(testSet.isActive());

        Assert.assertFalse(testSet.canClose());
        Assert.assertTrue(testSet.isClosed());

        testSet.reactivate();

        Assert.assertFalse(testSet.canReactivate());
        Assert.assertTrue(testSet.isActive());

        Assert.assertTrue(testSet.canClose());
        Assert.assertFalse(testSet.isClosed());
    }

    @Test
    public void copyAcceptanceTestsFromRegressionSuiteTest() {
        final String firstTestName = "test 1";
        final String secondTestName = "test 2";

        RegressionTest firstTest = getEntityFactory().createRegressionTest(firstTestName, getSandboxProject());
        RegressionTest secondTest = getEntityFactory().createRegressionTest(secondTestName, getSandboxProject());
        regressionSuite.getRegressionTests().add(firstTest);
        regressionSuite.getRegressionTests().add(secondTest);

        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite);
        testSet.copyAcceptanceTestsFromRegressionSuite();

        resetInstance();

        TestFilter filter = new TestFilter();
        filter.parent.add(testSet);
        Collection<com.versionone.om.Test> createdTests = getInstance().get().tests(filter);
        Assert.assertEquals(2, createdTests.size());

        Assert.assertTrue(containsRegressionTestReference(createdTests, firstTest));
        Assert.assertTrue(containsRegressionTestReference(createdTests, secondTest));
    }

    @Test
    public void canCloseTest() {
        TestSet testSet = getEntityFactory().createTestSet(TEST_SET_NAME, regressionSuite);

        Assert.assertTrue(testSet.canDelete());

        testSet.close();

        Assert.assertFalse(testSet.canDelete());

        testSet.reactivate();

        Assert.assertTrue(testSet.canDelete());
    }

    private static boolean containsRegressionTestReference(Collection<com.versionone.om.Test> tests,
                                                           RegressionTest referenceRegressionTest) {
        for (com.versionone.om.Test test : tests) {
            if (referenceRegressionTest.getGeneratedTests().contains(test)) {
                return true;
            }
        }
        return false;
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }
}
