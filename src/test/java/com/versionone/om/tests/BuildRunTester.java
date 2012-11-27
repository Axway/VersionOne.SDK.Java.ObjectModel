/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.AssetID;
import com.versionone.om.BuildProject;
import com.versionone.om.BuildRun;
import com.versionone.om.ChangeSet;
import com.versionone.om.Defect;
import com.versionone.om.Story;
import com.versionone.om.filters.DefectFilter;

import java.util.HashMap;
import java.util.Map;

public class BuildRunTester extends BaseSDKTester {

    private static final String MY_RUN = "My Run";
    private static final String DATE_2008_01_01 = "2008-01-01";
    private static final String PROJECT = "Project";
    private static final String MY_PROJECT = "My Project";
    private static final String TRIGGER = "Trigger";
    private static final String FAILED = "Failed";
    private static final String MY_REFERENCE = "My Reference";

    @Test
    public void testCreate() {
        BuildProject project = getInstance().create().buildProject(
                MY_PROJECT, PROJECT);
        BuildRun run = project.createBuildRun(MY_RUN, new DateTime(
                DATE_2008_01_01));

        run.setReference(MY_REFERENCE);
        run.setElapsed(5.0);
        run.getStatus().setCurrentValue(FAILED);
        run.getSource().setCurrentValue(TRIGGER);

        run.save();

        Assert.assertEquals(project, run.getBuildProject());
        Assert.assertEquals(MY_RUN, run.getName());
        Assert.assertEquals(new DateTime(DATE_2008_01_01), run.getDate());
        Assert.assertEquals(MY_REFERENCE, run.getReference());
        Assert.assertEquals(5., run.getElapsed(), ESTIMATES_PRECISION);
        Assert.assertEquals(FAILED, run.getStatus().getCurrentValue());
        Assert.assertEquals(TRIGGER, run.getSource().getCurrentValue());
    }

    @Test
    public void testCreateBuildRunWithAttributes() {
        final String description = "Test for BuildRun creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        BuildProject project = getInstance().create().buildProject(MY_PROJECT, PROJECT, attributes);
        BuildRun buildRun = project.createBuildRun("My Run", new DateTime(
                DATE_2008_01_01), attributes);

        buildRun.save();

        Assert.assertEquals(project, buildRun.getBuildProject());
        Assert.assertEquals(description, buildRun.getDescription());

        project.delete();
        buildRun.delete();
    }

    @Test
    public void testDelete() {
        BuildProject project = getInstance().create().buildProject(
                MY_PROJECT, PROJECT);
        BuildRun run = project.createBuildRun(MY_RUN, new DateTime(
                DATE_2008_01_01));
        AssetID id = run.getID();
        run.delete();

        resetInstance();
        Assert.assertNull(getInstance().get().buildRunByID(id));
    }

    @Test
    public void testGetAffectedPrimaryWorkitems() {
        final String testStory = "Test Story";
        final String testDefect = "Test Defect";
        final String otherStory = "Other Story";
        final String otherDefect = "Other Defect";
        final String testChangeSet = "Test ChangeSet";
        final String testChange1 = "123456";
        final String otherChangeSet = "Other ChangeSet";
        final String testChange2 = "abcd";
        final String bp = "BP";
        final String buildProjectName = "1234";
        final String br = "BR";
        final String notMyBr = "Not My BR";

        // Create Workitems
        Story story = getSandboxProject().createStory(testStory);
        Defect defect = getSandboxProject().createDefect(testDefect);
        Story notMyStory = getSandboxProject().createStory(otherStory);
        Defect notMyDefect = getSandboxProject().createDefect(otherDefect);

        // Changesets
        ChangeSet changeSet = getInstance().create().changeSet(
                testChangeSet, testChange1);
        ChangeSet notMyChangeSet = getInstance().create().changeSet(
                otherChangeSet, testChange2);
        changeSet.getPrimaryWorkitems().add(story);
        changeSet.getPrimaryWorkitems().add(defect);
        notMyChangeSet.getPrimaryWorkitems().add(notMyStory);
        notMyChangeSet.getPrimaryWorkitems().add(notMyDefect);

        // BuildRuns
        BuildProject buildProject = getInstance().create().buildProject(
                bp, buildProjectName);
        BuildRun buildRun = buildProject.createBuildRun(br, DateTime.now());
        BuildRun notMyBuildRun = buildProject.createBuildRun(notMyBr, DateTime
                .now());
        buildRun.getChangeSets().add(changeSet);
        notMyBuildRun.getChangeSets().add(notMyChangeSet);

        AssetID buildRunId = buildRun.getID();

        resetInstance();

        BuildRun theBuildRun = getInstance().get().buildRunByID(
                buildRunId);

        Assert.assertEquals(2, theBuildRun.getAffectedPrimaryWorkitems(null)
                .size());
        Assert.assertEquals(1, theBuildRun.getAffectedPrimaryWorkitems(
                new DefectFilter()).size());
    }
}
