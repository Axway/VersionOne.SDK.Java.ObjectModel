/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.DB.DateTime;
import com.versionone.apiclient.APIException;
import com.versionone.apiclient.ConnectionException;
import com.versionone.apiclient.IV1Configuration;
import com.versionone.apiclient.V1Exception;
import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class StoryTester extends BaseSDKTester {

    @Test
    public void storyOwner() {
        PrimaryWorkitem story = getSandboxProject().createStory("Add an owner to me");

        Member owner = getInstance().create().member("Dude", "dud", Role.PROJECT_LEAD);
        getSandboxProject().getAssignedMembers().add(owner);
        getSandboxProject().save();

        story.getOwners().add(owner);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        owner = getInstance().get().memberByID(owner.getID());

        Assert.assertTrue(findRelated(owner, story.getOwners()));
        Assert.assertTrue(findRelated(story, owner.getOwnedPrimaryWorkitems(null)));
    }

    @Test
    public void createAndRetrieveStory() {
        final String name = "New Story";

        AssetID id = getInstance().create().story(name, getSandboxProject()).getID();

        resetInstance();

        Story story = getInstance().get().storyByID(id);

        Assert.assertEquals(story.getName(), name);
    }

    @Test
    public void createStoryWithRequiredAttributes() {
        final String name = "New Story";

        AssetID id = getInstance().create().story(name, getSandboxProject(), null).getID();

        resetInstance();

        Story story = getInstance().get().storyByID(id);

        Assert.assertEquals(story.getName(), name);

        story.delete();
    }

    @Test
    public void storyOrder() {
        Story story1 = getSandboxProject().createStory("Story 1");
        Story story2 = getSandboxProject().createStory("Story 2");

        AssetID id1 = story1.getID();
        AssetID id2 = story2.getID();

        story1.getRankOrder().setBelow(story2);

        Assert.assertTrue(story1.getRankOrder().isBelow(story2));
        Assert.assertTrue(story2.getRankOrder().isAbove(story1));

        resetInstance();

        story1 = getInstance().get().storyByID(id1);
        story2 = getInstance().get().storyByID(id2);

        story1.getRankOrder().setAbove(story2);

        Assert.assertTrue(story1.getRankOrder().isAbove(story2));
        Assert.assertTrue(story2.getRankOrder().isBelow(story1));
    }

    @Test(expected = IllegalStateException.class)
    public void actuals() {
        Story story = getSandboxProject().createStory("Story 1");
        story.createEffort(5.); //Should throws
    }

    @Test(expected = IllegalStateException.class)
    public void actualsWithDateAndMember() {
        final DateTime date = new DateTime("2007-01-01");

        Member member = getInstance().get().memberByID("Member:20");
        Story story = getSandboxProject().createStory("Story 2");
        story.createEffort(10., member, date);  //Should throws
    }

    @Test
    public void getByDisplayID() {
        String defectName = "GetByDisplayIDTest";
        String displayID = getSandboxProject().createStory(defectName).getDisplayID();

        resetInstance();

        Assert.assertEquals(defectName, getInstance().get().
                storyByDisplayID(displayID).getName());
    }

    @Test(expected = IllegalStateException.class)
    public void honorTrackingLevelDetailEstimate() {
        // The V1SDKTests system is assumed to be configured for "Story:Off"
        Story story = getSandboxProject().createStory("Honor Tracking Level");
        story.setDetailEstimate(10.); //Should throws
    }

    @Test(expected = IllegalStateException.class)
    public void honorTrackingLevelToDo() {
        // The V1SDKTests system is assumed to be configured for "Story:Off"
        Story story = getSandboxProject().createStory("Honor Tracking Level");
        story.setToDo(10.); //Should throws
    }

    @Test(expected = IllegalStateException.class)
    public void honorTrackingLevelEffort() {
        // The V1SDKTests system is assumed to be configured for "Defect:On"
        Story story = getSandboxProject().createStory("Honor Tracking Level");
        story.createEffort(10.); // should throws
    }

    @Test(expected = ClassCastException.class)
    public void testValidObjectWrongType() {
        Epic epic = getEntityFactory().createEpic("test epic", getSandboxProject());
        Story testMe = getInstance().get().storyByID(epic.getID());
        Assert.assertNull(testMe);
    }

    @Test
    public void emptyAttributesTest() {
        final String name = "EmptyAttributesTest";
        Story story = getInstance().create().story(name, getSandboxProject());
        Assert.assertEquals(null, story.getReference());
        Assert.assertEquals(null, story.getEstimate());
        Assert.assertEquals(null, story.getDescription());

        story.setReference("ref");
        story.setEstimate(5d);
        story.setDescription("test");
        story.save();
        final AssetID id = story.getID();
        resetInstance();

        story = getInstance().get().storyByID(id);
        story.setReference(null);
        story.setEstimate(null);
        story.setDescription("");
        story.save();
        resetInstance();

        story = getInstance().get().storyByID(id);
        Assert.assertEquals(null, story.getReference());
        Assert.assertEquals(null, story.getEstimate());
        Assert.assertEquals(null, story.getDescription());

        story.delete();
    }

    /**
     * Test for defect D-01045 
     */
    @Test
    public void closeAndReactivateTest() {
        final String name = "CloseAndReactivateTest";
        Story story = getSandboxProject().createStory(name);
        Assert.assertFalse(story.canReactivate());
        Assert.assertTrue(story.canClose());
        Assert.assertFalse(story.isClosed());
        Assert.assertTrue(story.isActive());

        story.close();
        Assert.assertTrue(story.canReactivate());
        Assert.assertFalse(story.canClose());
        Assert.assertTrue(story.isClosed());
        Assert.assertFalse(story.isActive());

        story.reactivate();
        Assert.assertFalse(story.canReactivate());
        Assert.assertTrue(story.canClose());
        Assert.assertFalse(story.isClosed());
        Assert.assertTrue(story.isActive());

        story.delete();
    }

    @Test
    public void closeAndReactivateWorkitemTest() {
        final String name = "testWorkitemClose";
        getSandboxProject().createStory(name);
        final Collection<PrimaryWorkitem> workitems = getSandboxProject().getPrimaryWorkitems(null);
        Assert.assertEquals(1,workitems.size());
        PrimaryWorkitem story = workitems.iterator().next();

        Assert.assertFalse(story.canReactivate());
        Assert.assertTrue(story.canClose());
        Assert.assertFalse(story.isClosed());
        Assert.assertTrue(story.isActive());

        story.close();
        Assert.assertTrue(story.canReactivate());
        Assert.assertFalse(story.canClose());
        Assert.assertTrue(story.isClosed());
        Assert.assertFalse(story.isActive());

        story.reactivate();
        Assert.assertFalse(story.canReactivate());
        Assert.assertTrue(story.canClose());
        Assert.assertFalse(story.isClosed());
        Assert.assertTrue(story.isActive());

        story.delete();
    }

    /**
    * Test for D-01078 (CanDelete returns True on closed assets)
    **/
    @Test
    public void canDeleteTest() {
        final String name = "CanDeleteTest";
        getSandboxProject().createStory(name);
        Collection<PrimaryWorkitem> workitems = getSandboxProject().getPrimaryWorkitems(null);
        Assert.assertEquals(1, workitems.size());
        PrimaryWorkitem story = workitems.iterator().next();

        Assert.assertTrue(story.canDelete());

        story.close();
        Assert.assertFalse(story.canDelete());

        story.reactivate();
        Assert.assertTrue(story.canDelete());

        story.delete();
    }

    /**
    * Test for D-01143 (Effort Double Entry)
    **/
    @Test
    public void doubleEffortEntryTest() {
        final String storyName = "DoubleEffortEntryStoryTest";
        final String taskName = "DoubleEffortEntryTaskTest";

        try {
            if(!getInstance().getApiClient().getV1Config().isEffortTracking()){
                Assert.fail("Effort tracking is not enabled.");
            }
        } catch (V1Exception ex) {
            Assert.fail(ex.getMessage());
        }

        try {
            if(getInstance().getApiClient().getV1Config().getStoryTrackingLevel() == IV1Configuration.TrackingLevel.On){
                Assert.fail("Task effort tracking is not enabled.");
            }
        } catch (V1Exception ex) {
            Assert.fail(ex.getMessage());
        }

        Member member = getInstance().get().memberByID("Member:20");
        Story story = getSandboxProject().createStory(storyName);
        Task task = story.createTask(taskName);
        task.createEffort(1.0, member, DateTime.now());
        
        double actual = 0.0;
        Collection<Effort> effortRecords = task.getEffortRecords(null);

        for (Effort effortRecord : effortRecords){
            actual = actual + effortRecord.getValue();
        }

        Assert.assertEquals(1.0, actual, 0.1);

        story.delete();
    }

    @Test
    public void canBreakdown() {
        Story story = getEntityFactory().createStory("Story", getSandboxProject());
        Assert.assertTrue(story.canBreakdown());
    }

    @Test
    public void cannotBreakdown() {
        Story story = getEntityFactory().createStory("Story", getSandboxProject());
        story.close();
        Assert.assertFalse(story.canBreakdown());
    }
}
