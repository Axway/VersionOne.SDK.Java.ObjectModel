package com.versionone.om.tests;

import java.util.Collection;

import com.versionone.om.Epic;
import com.versionone.om.Project;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Story;
import com.versionone.om.filters.TestFilter;

public class TestFilterTester extends BaseSDKTester {
    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Test
    public void testType() {
        Story story = getEntityFactory().createStory("Type Filter", getSandboxProject());
        com.versionone.om.Test test = getEntityFactory().createTest("Type Filter", story);
        String taskType = test.getType().getAllValues()[0];
        test.getType().setCurrentValue(taskType);
        test.save();

        com.versionone.om.Test not = getEntityFactory().createTest("Doesn't match", story);

        resetInstance();

        TestFilter filter = new TestFilter();
        filter.type.add(taskType);

        Collection<com.versionone.om.Test> results = getSandboxProject().getTests(filter);

        Assert.assertTrue("Expected to find task that matched filter.", findRelated(test, results));
        Assert.assertFalse("Expected to NOT find task that doesn't match filter.", findRelated(not, results));
        for (com.versionone.om.Test result : results) {
            Assert.assertEquals(taskType, result.getType().getCurrentValue());
        }
    }

    @Test
    public void testStatus() {
        Story story = getEntityFactory().createStory("Status Filter", getSandboxProject());
        com.versionone.om.Test task = getEntityFactory().createTest("Status Filter", story);
        String taskStatus = task.getStatus().getAllValues()[0];
        task.getStatus().setCurrentValue(taskStatus);
        task.save();

        com.versionone.om.Test not = getEntityFactory().createTest("Doesn't match", story);

        resetInstance();

        TestFilter filter = new TestFilter();
        filter.status.add(taskStatus);

        Collection<com.versionone.om.Test> results = getSandboxProject()
                .getTests(filter);

        Assert.assertTrue("Expected to find task that matched filter.", findRelated(task, results));
        Assert.assertFalse("Expected to NOT find task that doesn't match filter.", findRelated(not, results));
        for (com.versionone.om.Test result : results) {
            Assert.assertEquals(taskStatus, result.getStatus()
                    .getCurrentValue());
        }
    }

    @Test
    public void epic() {
        Epic epic = getEntityFactory().createEpic("Epic for Test", getSandboxProject());
        com.versionone.om.Test test = getEntityFactory().createTest("test for Epic", epic);
        Epic notMatchEpic = getEntityFactory().createEpic("Doesn't match", getSandboxProject());
        com.versionone.om.Test notMatchTest = getEntityFactory().createTest("Doesn't match", notMatchEpic);

        resetInstance();

        TestFilter filter = new TestFilter();
        filter.epic.add(epic);

        Collection<com.versionone.om.Test> results = getSandboxProject().getTests(filter);
        Assert.assertEquals(1, results.size());
        Assert.assertTrue("Expected to find test that matched filter.", findRelated(test, results));
        Assert.assertFalse("Expected to NOT find test that doesn't match filter.", findRelated(notMatchTest, results));
        Assert.assertEquals(epic, first(results).getParent());
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }
}
