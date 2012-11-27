/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.IListValueProperty;
import com.versionone.om.PrimaryWorkitem;
import com.versionone.om.Project;
import com.versionone.om.Story;
import com.versionone.om.filters.PrimaryWorkitemFilter;

public class ListTypeTester extends BaseSDKTester {

    private static final String INVALID_WORKITEM_STATUS = "This Will Never be a valid workitem status";
    private static final String GOING_TO_FILTER_ON_STATUS = "Going to filter on Status";
    private static final String SDK_LIST_TYPE = "SDK List Type";
    private static final String IN_PROGRESS = "In Progress";

    @Test
    public void testGetListAttribute() {
        Project project = getSandboxProject();
        Story story = getInstance().create().story(SDK_LIST_TYPE,
                project);
        story.getStatus().setCurrentValue(IN_PROGRESS);
        story.save();

        IListValueProperty status = story.getStatus();
        Assert.assertEquals(IN_PROGRESS, status.getCurrentValue());
    }

    @Test
    public void testListTypeValues() {
        Story story = getInstance().create().story(SDK_LIST_TYPE,
                getSandboxProject());

        for (String value : story.getStatus().getAllValues()) {

            if (value.equals(IN_PROGRESS)) {
                story.getStatus().setCurrentValue(value);
            }
        }
        story.save();

        Assert.assertTrue("Expect \"" + IN_PROGRESS
                + "\" to be a valid Workitem Status", story.getStatus()
                .isValid(IN_PROGRESS));
        Assert.assertTrue("Expect null to be a valid Workitem Status", story
                .getStatus().isValid(null));
        Assert.assertFalse("Did not expect \"" + INVALID_WORKITEM_STATUS
                + "\" to be a valid Workitem Status", story.getStatus()
                .isValid(INVALID_WORKITEM_STATUS));
    }

    @Test
    public void testClearListAttribute() {
        Story story = getInstance().create().story(SDK_LIST_TYPE,
                getSandboxProject());
        story.getStatus().setCurrentValue(IN_PROGRESS);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertEquals(IN_PROGRESS, story.getStatus().getCurrentValue());
        story.getStatus().clearCurrentValue();
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertNull("Clear should remove the list value", story
                .getStatus().getCurrentValue());
        story.getStatus().setCurrentValue(IN_PROGRESS);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertEquals(IN_PROGRESS, story.getStatus().getCurrentValue());
        story.getStatus().setCurrentValue(null);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertNull("Clear should remove the list value", story
                .getStatus().getCurrentValue());
    }

    @Test
    public void testFilterListTypeText() {
        PrimaryWorkitem story = getSandboxProject().createStory(
                GOING_TO_FILTER_ON_STATUS);
        IListValueProperty st = story.getStatus();
        st.setCurrentValue(IN_PROGRESS);
        story.save();

        resetInstance();
        PrimaryWorkitemFilter filter = new PrimaryWorkitemFilter();
        filter.status.add(IN_PROGRESS);

        Collection<PrimaryWorkitem> result = getInstance().get()
                .primaryWorkitems(filter);

        Assert.assertTrue(findRelated(story, result));

        for (PrimaryWorkitem workitem : result) {
            Assert.assertEquals(IN_PROGRESS, workitem.getStatus().toString());
        }
    }
}
