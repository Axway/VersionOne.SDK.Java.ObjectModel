package com.versionone.om.tests;

import com.versionone.om.Member;
import com.versionone.om.Project;
import com.versionone.om.RegressionTest;
import com.versionone.om.Story;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RegressionTestTester extends BaseSDKTester {
    private final String regressionPlanName = "regression plan test";
    private final String regressionTestName = "My Regression Test";
    private final String updatedName = "New name";
    private final Project sandboxProject = getSandboxProject();
    private static Member member1;
    private static Member member2;

    @Before
    public void before() {
        member1 = getEntityFactory().createMember("member name 1");
        member2 = getEntityFactory().createMember("member name 2");
    }

    @Test
    public void create() {
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionPlanName, sandboxProject);
        Assert.assertEquals(regressionPlanName, regressionTest.getName());

        resetInstance();

        RegressionTest regressionTestNew = getInstance().get().regressionTestByID(regressionTest.getID());
        Assert.assertEquals(regressionPlanName, regressionTestNew.getName());
        Assert.assertEquals(getSandboxProject(), regressionTestNew.getProject());
    }

    @Test
    public void createWithAttributes() {
        String tags = "test tag regression";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Tags", tags);
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionPlanName, sandboxProject, attributes);
        Assert.assertEquals(regressionPlanName, regressionTest.getName());

        regressionTest.getOwners().add(member1);
        regressionTest.getOwners().add(member2);
        regressionTest.save();

        resetInstance();

        RegressionTest regressionTestNew = getInstance().get().regressionTestByID(regressionTest.getID());
        Assert.assertEquals(regressionPlanName, regressionTestNew.getName());
        Assert.assertEquals(sandboxProject, regressionTestNew.getProject());
        Assert.assertEquals(tags, regressionTestNew.getTags());
        Assert.assertEquals(2, regressionTestNew.getOwners().size());
    }

    @Test
    public void createBasedOnTest() {
        String storyName = "story name";
        String testName = "test name";

        Story story = getEntityFactory().createStory(storyName, sandboxProject);
        com.versionone.om.Test test = getEntityFactory().createTest(testName, story);
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(test);

        Assert.assertEquals(testName, regressionTest.getName());

        resetInstance();

        RegressionTest regressionTestNew = getInstance().get().regressionTestByID(regressionTest.getID());
        Assert.assertEquals(testName, regressionTestNew.getName());
        Assert.assertEquals(sandboxProject, regressionTestNew.getProject());
        Assert.assertEquals(test, regressionTestNew.getGeneratedFrom());


        test.getOwners().add(member1);
        test.getOwners().add(member2);
        test.save();

        RegressionTest regressionTest2 = getEntityFactory().createRegressionTest(test);
        Assert.assertEquals(2, regressionTest2.getOwners().size());
        ListAssert.contains(member1, regressionTest2.getOwners());
        ListAssert.contains(member2, regressionTest2.getOwners());

        resetInstance();

        RegressionTest regressionTestNew2 = getInstance().get().regressionTestByID(regressionTest2.getID());
        Assert.assertEquals(test, regressionTestNew.getGeneratedFrom());
        Assert.assertEquals(2, regressionTestNew2.getOwners().size());
        ListAssert.contains(member1, regressionTestNew2.getOwners());
        ListAssert.contains(member2, regressionTestNew2.getOwners());
    }

    @Test
    public void getFromServer() {
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionTestName, sandboxProject);

        resetInstance();

        Collection<RegressionTest> tests = getInstance().get().regressionTests(null);
        ListAssert.contains(regressionTest, tests);
    }

    @Test
    public void getFromServerById() {
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionPlanName, sandboxProject);

        resetInstance();

        RegressionTest queriedTest = getInstance().get().regressionTestByID(regressionTest.getID());
        Assert.assertEquals(regressionTest, queriedTest);
        Assert.assertEquals(regressionTest.getName(), queriedTest.getName());
    }

    @Test
    public void update() {
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionTestName, sandboxProject);

        regressionTest.getOwners().clear();
        regressionTest.getOwners().add(member1);
        regressionTest.setName(updatedName);
        regressionTest.save();

        resetInstance();

        RegressionTest testWithChanges = getInstance().get().regressionTestByID(regressionTest.getID());
        Assert.assertEquals(testWithChanges.getName(), updatedName);
        Assert.assertTrue(testWithChanges.getOwners().size() == 1);
        ListAssert.contains(member1, testWithChanges.getOwners());
    }

    @Test
    public void delete() {
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionTestName, sandboxProject);

        RegressionTest testWithChanges = getInstance().get().regressionTestByID(regressionTest.getID());
        Assert.assertNotNull(testWithChanges);

        resetInstance();

        testWithChanges.delete();
        Assert.assertNull(getInstance().get().regressionTestByID(testWithChanges.getID()));
    }

    @Test
    public void canClose() {
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionTestName, sandboxProject);

        Assert.assertTrue(regressionTest.canClose());
        Assert.assertFalse(regressionTest.isClosed());

        regressionTest.close();

        Assert.assertFalse(regressionTest.canClose());
        Assert.assertTrue(regressionTest.isClosed());
    }

    @Test
    public void canDelete() {
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionTestName, sandboxProject);

        Assert.assertTrue(regressionTest.canDelete());

        regressionTest.close();

        Assert.assertFalse(regressionTest.canDelete());
    }

    @Test
    public void canReactivate() {
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionTestName, sandboxProject);

        Assert.assertTrue(regressionTest.canDelete());
        Assert.assertFalse(regressionTest.canReactivate());
        Assert.assertTrue(regressionTest.isActive());

        regressionTest.close();

        Assert.assertFalse(regressionTest.canDelete());
        Assert.assertTrue(regressionTest.canReactivate());
        Assert.assertFalse(regressionTest.isActive());

        regressionTest.reactivate();

        Assert.assertTrue(regressionTest.canDelete());
        Assert.assertFalse(regressionTest.canReactivate());
        Assert.assertTrue(regressionTest.isActive());
        Assert.assertFalse(regressionTest.isClosed());
    }
}
