/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.Story;

public class OperationTester extends BaseSDKTester {

    private static final String MY_STORY = "My Story";
    private static final String SAMPLE_DATE = "2008-05-21T07:25:13";

    @Test
    public void testSaveBeforeClose() {
        String gend = new DateTime(SAMPLE_DATE).toString();
        Story story = getSandboxProject().createStory(MY_STORY);
        String storyid = story.getID().toString();
        story.setDescription(gend);
        story.close();

        resetInstance();

        Story newStory = getInstance().get().storyByID(storyid);
        Assert.assertEquals(gend, newStory.getDescription());
        Assert.assertTrue(newStory.isClosed());
    }

    @Test
    public void testSaveAfterOpen() {
        String gend = new DateTime(SAMPLE_DATE).toString();
        Story story = getSandboxProject().createStory(MY_STORY);
        String storyid = story.getID().toString();
        story.close();
        story.setDescription(gend);
        story.reactivate();

        resetInstance();

        Story newStory = getInstance().get().storyByID(storyid);
        Assert.assertEquals(gend, newStory.getDescription());
        Assert.assertTrue(newStory.isActive());
    }
}
