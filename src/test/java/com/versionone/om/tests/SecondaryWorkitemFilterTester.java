package com.versionone.om.tests;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.AssetID;
import com.versionone.om.SecondaryWorkitem;
import com.versionone.om.Story;
import com.versionone.om.Task;
import com.versionone.om.filters.SecondaryWorkitemFilter;
import com.versionone.om.filters.TaskFilter;
import com.versionone.om.filters.TestFilter;

public class SecondaryWorkitemFilterTester extends BaseSDKTester {

    @Test
    public void testFilterToAssetType() {
        int originalCount = getSandboxProject().getSecondaryWorkitems(null)
                .size();

        Story story = getSandboxProject().createStory("Should not show up");
        Task task = story.createTask("Task");
        com.versionone.om.Test test = story.createTest("Test");

        SecondaryWorkitemFilter filter = new SecondaryWorkitemFilter();
        filter.project.add(getSandboxProject());

        Collection<SecondaryWorkitem> workitems = getSandboxProject()
                .getSecondaryWorkitems(filter);

        try {
            Assert.assertEquals(originalCount + 2, workitems.size());
        } finally {
            test.delete();
            task.delete();
        }
    }

    private Story getStory(String name) {
        Story story = getSandboxProject().createStory(name);
        Task task = story.createTask("One");
        task.setBuild("12345");
        task.save();
        com.versionone.om.Test test1 = story.createTest("One");
        com.versionone.om.Test test2 = story.createTest("Two");
        test1.getRankOrder().setBelow(test2);
        return story;
    }

    @Test
    public void testParent() {
        Story story = getStory("Type Filter");

        resetInstance();

        SecondaryWorkitemFilter filter = new SecondaryWorkitemFilter();
        filter.parent.add(story);
        filter.name.add("One");

        Assert.assertEquals(2, getInstance().get().secondaryWorkitems(
                filter).size());
    }

    @Test
    public void testGetTasks() {
        Story story = getStory("GetTasks");
        AssetID id = story.getID();

        resetInstance();

        story = getInstance().get().storyByID(id);

        TaskFilter filter = new TaskFilter();

        // Make sure we can specify a value for a property that is not present
        // in SecondaryWorkitemFilter.
        filter.build.add("12345");

        Assert.assertEquals(1, story.getSecondaryWorkitems(filter).size());
    }

    @Test
    public void testGetTests() {
        Story story = getStory("GetTests");
        AssetID id = story.getID();

        resetInstance();

        story = getInstance().get().storyByID(id);

        SecondaryWorkitemFilter filter = new TestFilter();

        Assert.assertEquals(2, story.getSecondaryWorkitems(filter).size());
    }

    @Test
    public void testTestsInOrder() {
        Story story = getStory("TestsInOrder");
        TestFilter filter = new TestFilter();

        filter.orderBy.add("RankOrder");
        Collection<SecondaryWorkitem> items = story
                .getSecondaryWorkitems(filter);
        ListAssert.areEqual(new String[] { "Two", "One" }, items,
		new EntityToNameTransformer<SecondaryWorkitem>());
    }

	@Test
	public void testNoStoryAmongSecondaryWorkitems() {
		Story story = getStory("NoStoryAmongSecondaryWorkitems");

		resetInstance();

		ListAssert.notcontains(story.getName(), getInstance().get().secondaryWorkitems(null), new EntityToNameTransformer<SecondaryWorkitem>());
	}

}
