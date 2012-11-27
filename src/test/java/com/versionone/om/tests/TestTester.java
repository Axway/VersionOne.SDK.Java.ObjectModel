package com.versionone.om.tests;

import com.versionone.om.Epic;
import com.versionone.om.Member;
import com.versionone.om.Project;
import com.versionone.om.RegressionTest;
import com.versionone.om.SecondaryWorkitem;
import com.versionone.om.Story;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class TestTester extends BaseSDKTester {
    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Test
    public void generateRegressionTest() {
        String storyName = "story name";
        String testName = "test name";
        Story story = getEntityFactory().createStory(storyName, getSandboxProject());
        com.versionone.om.Test test = getEntityFactory().createTest(testName, story);
        RegressionTest regressionTest = test.generateRegressionTest();
        getEntityFactory().registerForDisposal(regressionTest);

        Assert.assertEquals(testName, regressionTest.getName());

        resetInstance();

        RegressionTest regressionTestNew = getInstance().get().regressionTestByID(regressionTest.getID());
        Assert.assertEquals(testName, regressionTestNew.getName());
        Assert.assertEquals(getSandboxProject(), regressionTestNew.getProject());
        Assert.assertEquals(test, regressionTestNew.getGeneratedFrom());

        Member member1 = getEntityFactory().createMember("member name 1");
        Member member2 = getEntityFactory().createMember("member name 2");

        test.getOwners().add(member1);
        test.getOwners().add(member2);
        test.save();

        RegressionTest regressionTest2 = test.generateRegressionTest();
        getEntityFactory().registerForDisposal(regressionTest2);

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
    public void createTestUnderStory() {
        Story story = getEntityFactory().createStory("Story", getSandboxProject());
        com.versionone.om.Test test = getEntityFactory().createTest("Test for story", story);
        resetInstance();

        Story updatedStory = getInstance().get().storyByID(story.getID());
        Collection<SecondaryWorkitem> children = updatedStory.getSecondaryWorkitems(null);

        Assert.assertEquals(1, children.size());
        Assert.assertEquals(test, first(children));
    }

    @Test
    public void moveTestFromOneEpicToAnother() {
        Epic epic1 = getEntityFactory().createEpic("first epic", getSandboxProject());
        Epic epic2 = getEntityFactory().createEpic("second epic", getSandboxProject());

        com.versionone.om.Test test = getEntityFactory().createTest("test for epic", epic1);
        resetInstance();

        Collection<com.versionone.om.Test> tests = epic1.getChildTests(null);
        com.versionone.om.Test testFromFirstEpic = first(tests);
        testFromFirstEpic.setParent(epic2);
        testFromFirstEpic.save();

        resetInstance();

        Collection<com.versionone.om.Test> testsFromSecondEpic = epic2.getChildTests(null);
        Assert.assertEquals(1, testsFromSecondEpic.size());
        Assert.assertEquals(test, first(testsFromSecondEpic));
    }

    @Test
    public void moveTestFromStoryToEpic() {
        Epic epic = getEntityFactory().createEpic("epic", getSandboxProject());
        Epic story = getEntityFactory().createEpic("story", getSandboxProject());

        com.versionone.om.Test test = getEntityFactory().createTest("test for story", story);
        resetInstance();

        Collection<com.versionone.om.Test> tests = story.getChildTests(null);
        com.versionone.om.Test testFromStory = first(tests);
        testFromStory.setParent(epic);
        testFromStory.save();

        resetInstance();

        Collection<com.versionone.om.Test> testsFromEpic = epic.getChildTests(null);
        Assert.assertEquals(1, testsFromEpic.size());
        Assert.assertEquals(test, first(testsFromEpic));
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }
}
