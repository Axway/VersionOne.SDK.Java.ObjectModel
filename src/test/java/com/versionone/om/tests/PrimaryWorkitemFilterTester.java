package com.versionone.om.tests;

import com.versionone.apiclient.FilterTerm;
import com.versionone.om.Epic;
import com.versionone.om.PrimaryWorkitem;
import com.versionone.om.Project;
import com.versionone.om.SecondaryWorkitem;
import com.versionone.om.Story;
import com.versionone.om.Task;
import com.versionone.om.Theme;
import com.versionone.om.Workitem;
import com.versionone.om.filters.BaseAssetFilter.State;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.filters.SecondaryWorkitemFilter;
import com.versionone.om.filters.StoryFilter;
import com.versionone.om.filters.TaskFilter;
import com.versionone.om.filters.WorkitemFilter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class PrimaryWorkitemFilterTester extends PrimaryWorkitemFilterTesterBase {
    private PrimaryWorkitemFilter GetFilter() {
        PrimaryWorkitemFilter filter = new PrimaryWorkitemFilter();
        filter.project.add(sandboxProject);
        return filter;
    }

    @Test
    public void project() {
        int actual = getInstance().get().primaryWorkitems(GetFilter()).size();
        Assert.assertEquals(6, actual);
    }

    @Test
    public void noOwner() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.owners.add(null);

        int actual = getInstance().get().primaryWorkitems(filter).size();
        Assert.assertEquals(2, actual);
    }

    @Test
    public void noOrAndreOwner() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.owners.add(null);
        filter.owners.add(andre);

        int actual = getInstance().get().primaryWorkitems(filter).size();
        Assert.assertEquals(5, actual);
    }

    @Test
    public void names() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.name.add("Defect 2");
        filter.name.add("Story 2");

        int actual = getInstance().get().primaryWorkitems(filter).size();
        Assert.assertEquals(2, actual);
    }

    @Test
    public void estimate() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.estimate.addTerm(FilterTerm.Operator.Equal, 1.0);
        Assert.assertEquals(1, getInstance().get().primaryWorkitems(filter).size());
    }

    @Test
    public void estimateRange() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.estimate.range(1.0, 3.0);
        Assert.assertEquals(2, getInstance().get().primaryWorkitems(filter).size());

        filter.estimate.clear();
        filter.estimate.range(1.5, 3.0);
        Assert.assertEquals(1, getInstance().get().primaryWorkitems(filter).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void estimateInvalidRange() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.estimate.range(2.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void estimateMinRangeBoundNull() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.estimate.range(null, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void estimateMaxRangeBoundNull() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.estimate.range(1.0, null);
    }

    @Test
    public void affectedBy() {
        PrimaryWorkitemFilter filter = GetFilter();
        filter.affectedByDefects.add(defect1);
        int actual = getInstance().get().primaryWorkitems(filter).size();
        Assert.assertEquals(1, actual);
    }

    @Test
    public void excludeThemesEpicsAndTemplates() {
        Project root = getSandboxProject().getParentProject();

        WorkitemFilter filter = new WorkitemFilter();
        filter.getState().add(State.Active);

        double totalEstimate = 0.;

        for (Workitem workitem : getInstance().get().workitems(filter)) {
            if (workitem.getDetailEstimate() != null) {
                totalEstimate += workitem.getDetailEstimate();
            }
        }

        final Double actual = root.getTotalDetailEstimate(filter, true);
        Assert.assertEquals(totalEstimate, actual, ESTIMATES_PRECISION);
    }

    @Test
    public void onlyStoriesAndDefects() {
        Project root = getSandboxProject().getParentProject();

        PrimaryWorkitemFilter filter = new PrimaryWorkitemFilter();
        filter.getState().add(State.Active);

        double totalEstimate = 0.;

        for (Workitem workitem : getInstance().get().workitems(filter)) {
            if (workitem.getDetailEstimate() != null) {
                totalEstimate += workitem.getDetailEstimate();
            }
        }

        final Double actual = root.getTotalDetailEstimate(filter, true);
        Assert.assertEquals(totalEstimate, actual, ESTIMATES_PRECISION);
    }

    @Test
    public void onlyTasksAndTests() {
        Project root = getSandboxProject().getParentProject();

        SecondaryWorkitemFilter filter = new SecondaryWorkitemFilter();
        filter.getState().add(State.Active);

        double totalEstimate = 0.;

        for (Workitem workitem : getInstance().get().workitems(filter)) {
            if (workitem.getDetailEstimate() != null) {
                totalEstimate += workitem.getDetailEstimate();
            }
        }

        final Double actual = root.getTotalDetailEstimate(filter, true);
        Assert.assertEquals(totalEstimate, actual, ESTIMATES_PRECISION);
    }

    @Test
    public void taskDefaultOrder() {
        Story story = getSandboxProject().createStory("My Story");
        Task task1 = story.createTask("Task 1");
        Task task2 = story.createTask("Task 2");
        Task task3 = story.createTask("Task 3");

        task2.getRankOrder().setBelow(task3);
        task1.getRankOrder().setBelow(task2);

        // order should be 3,2,1
        Collection<SecondaryWorkitem> workitems = story.getSecondaryWorkitems(new TaskFilter());
        Object[] expected = new String[]{"Task 3", "Task 2", "Task 1"};
        ListAssert.areEqual(expected, workitems, new EntityToNameTransformer<SecondaryWorkitem>());
    }

    /**
     * Tests passing a more specific filter to a less specific query method.
     */
    @Test
    public void taskBuildFilter() {
        final String strTaskB = "Task B";
        final String strTaskD = "Task D";
        final String strBuildBeta = "Build Beta";

        Story story = getSandboxProject().createStory("Task Builds");
        Task taskA = story.createTask("Task A");
        Task taskB = story.createTask(strTaskB);
        Task taskC = story.createTask("Task C");
        Task taskD = story.createTask(strTaskD);

        taskA.setBuild("Build Alpha");
        taskB.setBuild(strBuildBeta);
        taskC.setBuild("Build Alpha");
        taskD.setBuild(strBuildBeta);

        taskA.save();
        taskB.save();
        taskC.save();
        taskD.save();

        TaskFilter filter = new TaskFilter();
        filter.build.add(strBuildBeta);
        Collection<SecondaryWorkitem> items = story.getSecondaryWorkitems(filter);
        ListAssert.areEqual(new Object[]{"Task B", "Task D"}, items, new EntityToNameTransformer<SecondaryWorkitem>());
    }

    @Test
    public void noEpicAmongPrimaryWorkitems() {
        Epic epic = getSandboxProject().createEpic("War and Piece");

        resetInstance();

        ListAssert.notcontains(epic.getName(), getInstance().get().primaryWorkitems(null), new EntityToNameTransformer<PrimaryWorkitem>());
    }

    @Test
    public void themes() {
        final StoryFilter filter = new StoryFilter();
        final Theme theme = getSandboxProject().createTheme("Test Theme");
        filter.theme.add(theme);

        final Collection<Story> stories = getInstance().get().story(filter);
        Assert.assertEquals(0, stories.size());

        story1.setTheme(theme);
        story1.save();
        resetInstance();

        final Collection<Story> storiesWithTestTheme = getInstance().get().story(filter);
        Assert.assertEquals(1, storiesWithTestTheme.size());
        Assert.assertEquals(story1, storiesWithTestTheme.iterator().next());
    }
}
