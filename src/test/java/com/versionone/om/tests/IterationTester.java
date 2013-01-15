/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Ignore;

import com.versionone.DB.DateTime;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.filters.StoryFilter;

public class IterationTester extends BaseSDKTester {

    private static final String TIMEBOX_1034 = "Timebox:1034";

    @Override
    protected Project createSandboxProject(Project rootProject) {
    	Map<String, Object> attributes = new HashMap<String, Object>();
    	attributes.put("Scheme", getDefaultSchemeOid());
        return getInstance().create().project(getSandboxName(), rootProject, DateTime.now(), getSandboxSchedule(), attributes);
    }

    @Test
    public void testIterationAttributes() {
        final String Timebox = "Timebox:1025";
        final String MonthC1stHalf = "Month C 1st Half";

        Iteration iteration = getInstance().get().iterationByID(Timebox);
        Assert.assertEquals(MonthC1stHalf, iteration.getName());
        Assert.assertFalse(iteration.isFuture());
        Assert.assertTrue(iteration.isActive());
        Assert.assertFalse(iteration.isClosed());
        Assert.assertTrue(iteration.canMakeFuture());
        Assert.assertTrue(iteration.canClose());
        Assert.assertFalse(iteration.canActivate());
        Assert.assertFalse(iteration.canReactivate());
    }

    @Test
    public void testCreateIterationWithAttributes() {
        final String description = "Test for Iteration creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        getInstance().setValidationEnabled(true);

        Iteration iteration = getSandboxProject().createIteration(attributes);
        AssetID iterationID = iteration.getID();

        resetInstance();

        iteration = getInstance().get().iterationByID(iterationID);

        Assert.assertEquals(description, iteration.getDescription());
        Assert.assertEquals(getSandboxProject().getSchedule(), iteration.getSchedule());

        iteration.delete();
        getInstance().setValidationEnabled(false);
    }

    @Test
    public void testCreateIterationWithReqAttributes() {
        final String name = "IterationName";
        final DateTime dateStart = new DateTime("2008-03-05");
        final DateTime dateEnd = new DateTime("2008-05-03");
        final String description = "Test for Iteration creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        getInstance().setValidationEnabled(true);

        Iteration iteration = getSandboxProject().createIteration(name, dateStart, dateEnd, attributes);
        AssetID iterationID = iteration.getID();

        resetInstance();

        iteration = getInstance().get().iterationByID(iterationID);

        Assert.assertEquals(name, iteration.getName());
        Assert.assertEquals(description, iteration.getDescription());
        Assert.assertEquals(dateEnd, iteration.getEndDate());
        Assert.assertEquals(dateStart, iteration.getBeginDate());
        Assert.assertEquals(getSandboxProject().getSchedule(), iteration.getSchedule());

        iteration.delete();
        getInstance().setValidationEnabled(false);
    }

    @Test
    public void testCreateWithSystemSuggested() {
        final String scope = "Scope:1018";
        final String iterationName = "CreateWithSystemSuggestedTest";
        final String beginDate = "2008-01-26";
        final String endDate = "2008-02-02";

        Project project = getInstance().get().projectByID(scope);
        Iteration iteration = project.createIteration(iterationName, new DateTime(beginDate), new DateTime(endDate));
        String iterationID = iteration.getID().toString();

        resetInstance();

        Project retrievedProject = getInstance().get().projectByID(scope);
        Iteration retrievedIteration = getInstance().get().iterationByID(iterationID);

        Assert.assertEquals(retrievedProject.getSchedule(), retrievedIteration.getSchedule());
        Assert.assertEquals(iterationName, retrievedIteration.getName());
        Assert.assertEquals(new DateTime(beginDate), retrievedIteration
                .getBeginDate());
        Assert.assertEquals(new DateTime(endDate), retrievedIteration.getEndDate());
    }

    //@Ignore("Exception:  Error writing to output stream.") 
    @Test
    public void testCreateCustomIteration() {
        final String scope = "Scope:1018";
        final String iterationName = "Month 7 & 8";
        final String dateStart = "2008-03-05";
        final String dateEnd = "2008-05-03";

        Project project = getInstance().get().projectByID(scope);
        Iteration iteration = project.createIteration(iterationName,
                new DateTime(dateStart), new DateTime(dateEnd));
        String iterationID = iteration.getID().toString();

        resetInstance();

        Project newProject = getInstance().get().projectByID(scope);
        Iteration newIteration = getInstance().get().iterationByID(
                iterationID);

        Assert.assertEquals(newProject.getSchedule(), newIteration.getSchedule());
        Assert.assertEquals(iterationName, newIteration.getName());
        Assert.assertEquals(new DateTime(dateStart), newIteration
                .getBeginDate());
        Assert.assertEquals(new DateTime(dateEnd), newIteration.getEndDate());
    }

    @Test
    public void testEnumerateStoriesAndDefects() {
        Collection<PrimaryWorkitem> items = getInstance().get()
                .iterationByID("Timebox:1026").getPrimaryWorkitems(null);
        String[] expected = new String[] { "Story:1084", "Story:1085",
                "Story:1086", "Story:1087", "Defect:1411", "Defect:1412",
                "Defect:1413" };
        ListAssert.areEqualIgnoringOrder(expected, items,
                new EntityToAssetIDTransformer<PrimaryWorkitem>());
    }

    @Test
    public void testAssignStory() {
        Iteration iteration = getInstance().get().iterationByID(
                TIMEBOX_1034);
        Project project = first(iteration.getSchedule().getScheduledProjects());
        Story story = project.createStory("New Story");
        String storyID = story.getID().toString();
        story.setIteration(iteration);
        story.save();

        resetInstance();

        Assert.assertEquals(getInstance().get().iterationByID(
                TIMEBOX_1034), getInstance().get().storyByID(storyID)
                .getIteration());
    }

    @Test
    public void testAssignDefect() {
        Iteration iteration = getInstance().get().iterationByID(
                TIMEBOX_1034);
        Project project = first(iteration.getSchedule().getScheduledProjects());
        Defect defect = project.createDefect("New Defect");
        String defectID = defect.getID().toString();
        defect.setIteration(iteration);
        defect.save(); 

        resetInstance();

        Assert.assertEquals(getInstance().get().iterationByID(
                TIMEBOX_1034), getInstance().get().defectByID(defectID)
                .getIteration());
    }

    @Test
    public void testGetStories() {
        Iteration iteration = getEntityFactory().create(new EntityFactory.IEntityCreator<Iteration>() {
            public Iteration create() {
                return getSandboxProject().createIteration();
            }
        });
        iteration.setName("Test Iteration");
        iteration.save();

        createStory("Story 1", getSandboxProject(), iteration);
        createStory("Story 2", getSandboxProject(), iteration);

        Assert.assertEquals(2, iteration.getPrimaryWorkitems(new StoryFilter())
                .size());

        StoryFilter filter = new StoryFilter();
        filter.iteration.add(getSandboxProject().createIteration());
        Assert.assertEquals(
                "Iteration.GetStories didn't override the Iteration filter.",
                2, iteration.getPrimaryWorkitems(filter).size());
    }

    @Test
    public void testGetPrimaryWorkitems() {
        Iteration iteration = getEntityFactory().create(new EntityFactory.IEntityCreator<Iteration>() {
            public Iteration create() {
                return getSandboxProject().createIteration();
            }
        });
        iteration.setName("Test Iteration");
        iteration.save();

        createStory("Story 1", getSandboxProject(), iteration);
        createStory("Story 2", getSandboxProject(), iteration);
        createDefect("Defect 1", getSandboxProject(), iteration);
        createDefect("Defect 1", getSandboxProject(), iteration);

        Assert.assertEquals(4, iteration.getPrimaryWorkitems(
                new PrimaryWorkitemFilter()).size());
    }
}
