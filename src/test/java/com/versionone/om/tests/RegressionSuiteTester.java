package com.versionone.om.tests;

import com.versionone.Oid;
import com.versionone.apiclient.OidException;
import com.versionone.om.*;
import com.versionone.om.filters.TestSetFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegressionSuiteTester extends BaseSDKTester {
    private RegressionPlan regressionPlan;
    private Member member;
    private final String REGRESSION_SUITE_NAME = "Regression Suite Name";
    private final String REGRESSION_SUITE_DESCRIPTION = "Description for Regression Suite";
    private final String REGRESSION_SUITE_REFERENCE = "Reference for Regression Suite";
    private final String TEST_SET_NAME = "test set 1";

    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }

    @Before
    public void before() {
        regressionPlan = getEntityFactory().createRegressionPlan("test plan", getSandboxProject());
        member = getEntityFactory().createMember("test user");
    }

    @Test
    public void createRegressionSuiteTest() {
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME,
                                                                                   regressionPlan);
        Assert.assertEquals(REGRESSION_SUITE_NAME, regressionSuite.getName());
        Assert.assertEquals(regressionPlan, regressionSuite.getRegressionPlan());
    }

    @Test
    public void createRegressionSuiteWithAttributesTest() throws OidException {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", REGRESSION_SUITE_DESCRIPTION);
        attributes.put("Reference", REGRESSION_SUITE_REFERENCE);
        attributes.put("Owner", Oid.fromToken(member.getID().toString(),
                                              getInstance().getApiClient().getMetaModel()));
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME,
                                                                                   regressionPlan, attributes);

        Assert.assertEquals(REGRESSION_SUITE_NAME, regressionSuite.getName());
        Assert.assertEquals(REGRESSION_SUITE_DESCRIPTION, regressionSuite.getDescription());
        Assert.assertEquals(REGRESSION_SUITE_REFERENCE, regressionSuite.getReference());
        Assert.assertEquals(member, regressionSuite.getOwner());
    }

    @Test
    public void updateRegressionSuiteTest() {
        final String addonForName = "Updated";
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME,
                                                                                   regressionPlan);
        resetInstance();

        RegressionSuite regressionSuiteNew = getInstance().get().regressionSuiteByID(regressionSuite.getID());
        regressionSuiteNew.setName(REGRESSION_SUITE_NAME + addonForName);
        regressionSuiteNew.setDescription(REGRESSION_SUITE_DESCRIPTION);
        regressionSuiteNew.setReference(REGRESSION_SUITE_REFERENCE);
        regressionSuiteNew.setOwner(member);
        regressionSuiteNew.save();

        resetInstance();

        regressionSuite = getInstance().get().regressionSuiteByID(regressionSuite.getID());
        Assert.assertEquals(REGRESSION_SUITE_NAME + addonForName, regressionSuite.getName());
        Assert.assertEquals(REGRESSION_SUITE_DESCRIPTION, regressionSuite.getDescription());
        Assert.assertEquals(REGRESSION_SUITE_REFERENCE, regressionSuite.getReference());
        Assert.assertEquals(member, regressionSuite.getOwner());
    }

    @Test
    public void deleteRegressionSuiteTest() {
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME,
                                                                                   regressionPlan);
        resetInstance();

        RegressionSuite regressionSuiteNew = getInstance().get().regressionSuiteByID(regressionSuite.getID());
        regressionSuiteNew.delete();

        regressionSuiteNew = getInstance().get().regressionSuiteByID(regressionSuite.getID());
        Assert.assertNull(regressionSuiteNew);
    }

    @Test
    public void createTestSetTest() {
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME,
                                                                                   regressionPlan);
        TestSet testSet = regressionSuite.createTestSet(TEST_SET_NAME);
        getEntityFactory().registerForDisposal(testSet);

        resetInstance();

        TestSetFilter filter = new TestSetFilter();
        filter.regressionSuite.add(regressionSuite);
        List<TestSet> testSets = new ArrayList<TestSet>(getInstance().get().testSets(filter));
        ListAssert.contains(testSet, testSets);
        Assert.assertTrue(testSets.get(0).getRegressionSuite().equals(regressionSuite));
    }

    @Test
    public void assignRegressionTest() {
        String regressionTestName = "regression test for assign N";
        List<RegressionTest> regressionTests = new ArrayList<RegressionTest>(4);
        // create 4 regression tests
        for (int i = 0; i < 4; i++) {
            regressionTests.add(getEntityFactory().createRegressionTest(regressionTestName + i, getSandboxProject()));
        }
        RegressionSuite regressionSuite =
                getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME, regressionPlan);

        Assert.assertEquals(0, regressionSuite.getRegressionTests().size());
        // assign 3 regression tests
        for (int i = 0; i < 3; i++) {
            regressionSuite.assignRegressionTest(regressionTests.get(i));
        }
        regressionSuite.save();
        Assert.assertEquals(3, regressionSuite.getRegressionTests().size());

        resetInstance();

        RegressionSuite regressionSuiteNew = getInstance().get().regressionSuiteByID(regressionSuite.getID());
        Assert.assertEquals(3, regressionSuiteNew.getRegressionTests().size());
        // test that all regression tests have info about regression suite
        for (int i = 0; i < 3; i++) {
            RegressionTest regressionTest = getInstance().get().regressionTestByID(regressionTests.get(i).getID());
            Assert.assertEquals(1, regressionTest.getRegressionSuites().size());
            ListAssert.contains(regressionSuite, regressionTest.getRegressionSuites());
            ListAssert.contains(regressionTest, regressionSuite.getRegressionTests());
        }
        RegressionTest regressionTestNotAssigned =
                getInstance().get().regressionTestByID(regressionTests.get(3).getID());
        ListAssert.notcontains(regressionTestNotAssigned, regressionSuite.getRegressionTests());
    }

    @Test(expected = IllegalArgumentException.class)
    public void unassignRegressionTest() {
        String regressionTestName = "regression test for unassign N";
        List<RegressionTest> regressionTests = new ArrayList<RegressionTest>(4);
        RegressionSuite regressionSuite =
                getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME, regressionPlan);
        // create and assign 4 regression tests
        for (int i = 0; i < 4; i++) {
            RegressionTest regTest = getEntityFactory().createRegressionTest(regressionTestName + i,
                                                                             getSandboxProject());
            regressionTests.add(regTest);
            // assign all regression tests except 4th
            if (i != 3) {
                regressionSuite.assignRegressionTest(regTest);
            }
        }
        regressionSuite.save();

        resetInstance();

        RegressionSuite regressionSuiteNew = getInstance().get().regressionSuiteByID(regressionSuite.getID());
        Assert.assertEquals(3, regressionSuiteNew.getRegressionTests().size());
        // unassign 3 regression tests
        for (int i = 0; i < 3; i++) {
            regressionSuite.unassignRegressionTest(regressionTests.get(i));
        }
        regressionSuite.save();

        resetInstance();

        regressionSuiteNew = getInstance().get().regressionSuiteByID(regressionSuite.getID());
        Assert.assertEquals(0, regressionSuiteNew.getRegressionTests().size());
        // test that all regression tests don't have info about regression suite
        for (int i = 0; i < 3; i++) {
            RegressionTest regressionTest = getInstance().get().regressionTestByID(regressionTests.get(i).getID());
            Assert.assertEquals(0, regressionTest.getRegressionSuites().size());
            ListAssert.notcontains(regressionSuite, regressionTest.getRegressionSuites());
            ListAssert.notcontains(regressionTest, regressionSuite.getRegressionTests());
        }
        regressionSuite.unassignRegressionTest(regressionTests.get(3));
        //Assert.fail("This place can't be reached");
    }

    @Test
    public void assignUnassignRegressionCachingTest() {
        String regressionTestName = "regression test for caching N";
        List<RegressionTest> regressionTests = new ArrayList<RegressionTest>(4);
        RegressionSuite regressionSuite =
                getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME, regressionPlan);
        // create and assign 4 regression tests
        for (int i = 0; i < 4; i++) {
            RegressionTest regTest = getEntityFactory().createRegressionTest(regressionTestName + i,
                                                                             getSandboxProject());
            regressionTests.add(regTest);
            regressionSuite.assignRegressionTest(regTest);
        }

        resetInstance();

        RegressionSuite regressionSuiteNew = getInstance().get().regressionSuiteByID(regressionSuite.getID());
        Assert.assertEquals(4, regressionSuiteNew.getRegressionTests().size());
        //all regression tests assigned to regression suite
        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(1, regressionTests.get(i).getRegressionSuites().size());
        }
        // unassign 2 regression tests
        for (int i = 0; i < 2; i++) {
            regressionSuite.unassignRegressionTest(regressionTests.get(i));
        }
        Assert.assertEquals(0, regressionTests.get(0).getRegressionSuites().size());
        Assert.assertEquals(0, regressionTests.get(1).getRegressionSuites().size());
        Assert.assertEquals(1, regressionTests.get(2).getRegressionSuites().size());
        Assert.assertEquals(1, regressionTests.get(3).getRegressionSuites().size());

        Assert.assertEquals(2, regressionSuite.getRegressionTests().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void canNotCloseTest() {
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME,
                                                                                regressionPlan);
        Assert.assertTrue(regressionSuite.isActive());
        Assert.assertFalse(regressionSuite.isClosed());

        Assert.assertFalse(regressionSuite.canClose());
        Assert.assertTrue(regressionSuite.canDelete());

        regressionSuite.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void canNotReactivateTest() {
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite(REGRESSION_SUITE_NAME,
                                                                                regressionPlan);
        Assert.assertFalse(regressionSuite.canReactivate());
        regressionSuite.reactivate();
    }
}
