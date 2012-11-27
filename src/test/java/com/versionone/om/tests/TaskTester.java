/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.versionone.om.Project;
import com.versionone.om.SDKException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Story;
import com.versionone.om.Task;
import com.versionone.om.filters.TaskFilter;

public class TaskTester extends BaseSDKTester {
    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    private Task[] getTaskArrayFromFilter(TaskFilter filter) {
        Collection<Task> tasks = getInstance().get().tasks(filter);
        Task[] taskArray = new Task[tasks.size()];
        return tasks.toArray(taskArray);
    }

    @Test
    public void testOrder() {
        final String strTaskName1 = "Task 1";
        final String strTaskName2 = "Task 2";

        Story story = getEntityFactory().createStory("Task Order Test", getSandboxProject());

        Task task1 = getEntityFactory().createTask(strTaskName1, story, null);
        Task task2 = getEntityFactory().createTask(strTaskName2, story, null);

        TaskFilter filter = new TaskFilter();
        filter.parent.add(story);
        filter.orderBy.add("RankOrder");
        Task[] taskArray = getTaskArrayFromFilter(filter);
        Assert.assertEquals(strTaskName1, taskArray[0].getName());
        Assert.assertEquals(strTaskName2, taskArray[1].getName());

        task2.getRankOrder().setAbove(task1);

        taskArray = getTaskArrayFromFilter(filter);
        Assert.assertEquals(strTaskName2, taskArray[0].getName());
        Assert.assertEquals(strTaskName1, taskArray[1].getName());
    }

    @Test
    public void testCreateTaskWithAttributes() {
        final String name = "Task Name";
        final Story story = getEntityFactory().createStory("Task Order Test", getSandboxProject());
        final String description = "Test for Task creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        Task task = getEntityFactory().createTask(name, story, attributes);

        Assert.assertEquals(name, task.getName());
        Assert.assertEquals(description, task.getDescription());

        task.delete();
    }

    @Test(expected=SDKException.class)
    public void taskAsParentForTask() {
        Story story = getEntityFactory().createStory("Story for task as task", getSandboxProject());
        Task task = getEntityFactory().createTask("Task for task", story, null);

        task.setParent(task);
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }
}
