package com.versionone.om.tests;

import com.versionone.DB;
import static com.versionone.apiclient.FilterTerm.Operator.GreaterThan;
import static com.versionone.apiclient.FilterTerm.Operator.GreaterThanOrEqual;
import static com.versionone.apiclient.FilterTerm.Operator.LessThan;
import static com.versionone.apiclient.FilterTerm.Operator.LessThanOrEqual;
import com.versionone.om.Story;
import com.versionone.om.Task;
import com.versionone.om.filters.TaskFilter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class TaskFilterTester extends BaseSDKTester {

    @Test
    public void testBuild() {
        final String strBuild = "10.2.24.1";
        Story story = getSandboxProject().createStory("Build Filter");
        Task task = story.createTask("Build Filter");

        task.setBuild(strBuild);
        task.save();

        Task not = story.createTask("Doesn't match");

        resetInstance();

        TaskFilter filter = new TaskFilter();
        filter.build.add(strBuild);

        Collection<Task> results = getSandboxProject().getTasks(filter);

        Assert.assertTrue("Expected to find task that matched filter.",
                findRelated(task, results));
        Assert.assertFalse(
                "Expected to NOT find task that doesn't match filter.",
                findRelated(not, results));
        for (Task result : results) {
            Assert.assertEquals(strBuild, result.getBuild());
        }
    }

    @Test
    public void testSource() {
        Story story = getSandboxProject().createStory("Source Filter");
        Task task = story.createTask("Source Filter");
        String taskSource = task.getSource().getAllValues()[0];
        task.getSource().setCurrentValue(taskSource);
        task.save();

        Task not = story.createTask("Doesn't match");

        resetInstance();

        TaskFilter filter = new TaskFilter();
        filter.source.add(taskSource);

        Collection<Task> results = getSandboxProject().getTasks(filter);

        Assert.assertTrue("Expected to find task that matched filter.",
                findRelated(task, results));
        Assert.assertFalse(
                "Expected to NOT find task that doesn't match filter.",
                findRelated(not, results));
        for (Task result : results) {
            Assert.assertEquals(taskSource, result.getSource()
                    .getCurrentValue());
        }
    }

    @Test
    public void testType() {
        Story story = getSandboxProject().createStory("Type Filter");
        Task task = story.createTask("Type Filter");
        String taskType = task.getType().getAllValues()[0];
        task.getType().setCurrentValue(taskType);
        task.save();

        Task not = story.createTask("Doesn't match");

        resetInstance();

        TaskFilter filter = new TaskFilter();
        filter.type.add(taskType);

        Collection<Task> results = getSandboxProject().getTasks(filter);

        Assert.assertTrue("Expected to find task that matched filter.",
                findRelated(task, results));
        Assert.assertFalse(
                "Expected to NOT find task that doesn't match filter.",
                findRelated(not, results));
        for (Task result : results) {
            Assert.assertEquals(taskType, result.getType().getCurrentValue());
        }
    }

    @Test
    public void testStatus() {
        Story story = getSandboxProject().createStory("Status Filter");
        Task task = story.createTask("Status Filter");
        String taskStatus = task.getStatus().getAllValues()[0];
        task.getStatus().setCurrentValue(taskStatus);
        task.save();

        Task not = story.createTask("Doesn't match");

        resetInstance();

        TaskFilter filter = new TaskFilter();
        filter.status.add(taskStatus);

        Collection<Task> results = getSandboxProject().getTasks(filter);

        Assert.assertTrue("Expected to find task that matched filter.",
                findRelated(task, results));
        Assert.assertFalse(
                "Expected to NOT find task that doesn't match filter.",
                findRelated(not, results));
        for (Task result : results) {
            Assert.assertEquals(taskStatus, result.getStatus()
                    .getCurrentValue());
        }
    }

    @Test
    public void testParent() {
        Story story = getSandboxProject().createStory("Type Filter");
        story.createTask("Task 1");
        story.createTask("Task 2");

        resetInstance();

        TaskFilter filter = new TaskFilter();
        filter.parent.add(story);

        Assert.assertEquals(2, getInstance().get().tasks(filter).size());
    }
}
