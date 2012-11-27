/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.*;

import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.DB.DateTime;

public class RetrospectiveTester extends BaseSDKTester {
    private static final String NEW_RETRO = "New Retro";

    @Override 
    protected Project createSandboxProject(Project rootProject) {
    	Map<String, Object> attributes = new HashMap<String, Object>();
    	attributes.put("Scheme", getDefaultSchemeOid());
        return getInstance().create().project(getSandboxName(), rootProject, DateTime.now(), getSandboxSchedule(), attributes);
    }

    @Test
    public void testRetrospectiveAttributes() {
        Retrospective retro = getInstance().get().retrospectiveByID(
                "Retrospective:1789");
        Assert.assertEquals("First Retrospective", retro.getName());
        Assert.assertTrue(retro.canClose());
        Assert.assertFalse(retro.canReactivate());
    }

    @Test
    public void testCreate() {
        Retrospective retro = getSandboxProject().createRetrospective(NEW_RETRO);
        String retroID = retro.getID().toString();

        resetInstance();

        Retrospective newRetro = getInstance().get().retrospectiveByID(
                retroID);
        Assert.assertEquals(newRetro.getName(), NEW_RETRO);
        Assert.assertEquals(getSandboxProject(), newRetro.getProject());
    }

    @Test
    public void testCreateRetrospectiveWithAttributes() {
        final String name = "TestRetrospectiveName";
        final Project project = getInstance().get().projectByID(SCOPE_ZERO);
        final String description = "Test for Retrospective creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        Retrospective retrospective = getSandboxProject().createRetrospective(name, attributes);

        Assert.assertEquals(name, retrospective.getName());
        Assert.assertEquals(description, retrospective.getDescription());

        retrospective.delete();
    }

    @Test
    public void testBasicAttributes() {
        Retrospective retro = getSandboxProject().createRetrospective(NEW_RETRO);

        final DateTime today = DateTime.now().getDate();
        Iteration retroIteration = getSandboxProject().createIteration(
                "Retro Iteration", today, today.add(Calendar.DAY_OF_MONTH, 7));
        Member facilitator = getInstance().get().memberByID("Member:20");
        String retroSummary = "We did this, we did that...";

        retro.setSummary(retroSummary);
        retro.setDate(today);
        retro.setFacilitatedBy(facilitator);
        retro.setIteration(retroIteration);

        retro.save();

        resetInstance();

        Retrospective newRetro = getInstance().get().retrospectiveByID(
                retro.getID());
        retroIteration = getInstance().get().iterationByID(
                retroIteration.getID());
        facilitator = getInstance().get().memberByID(facilitator.getID());

        Assert.assertEquals(retroIteration, newRetro.getIteration());
        Assert.assertEquals(facilitator, newRetro.getFacilitatedBy());
        Assert.assertEquals(today, newRetro.getDate());
        Assert.assertEquals(retroSummary, newRetro.getSummary());
    }

    @Test
    public void testIdentifiedStories() {
        Retrospective retro = getSandboxProject().createRetrospective(
                "Retro with Stories");
        Story story = retro.createStory("Retro Story");

        resetInstance();

        retro = getInstance().get().retrospectiveByID(retro.getID());
        story = getInstance().get().storyByID(story.getID());

        Assert.assertTrue(findRelated(story, retro.getIdentifiedStories(null)));
        Assert.assertEquals(story.getIdentifiedIn(), retro);
    }

    @Test
    public void testIssues() {
        Retrospective retro = getSandboxProject().createRetrospective(
                "Retro with Issues");
        Issue issue = retro.createIssue("Retro Issue");

        resetInstance();

        retro = getInstance().get().retrospectiveByID(retro.getID());
        issue = getInstance().get().issueByID(issue.getID());

        Assert.assertTrue(findRelated(issue, retro.getIssues(null)));
        Assert.assertTrue(findRelated(retro, issue.getRetrospectives()));
    }
}
