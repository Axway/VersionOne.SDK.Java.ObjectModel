/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.AssetID;
import com.versionone.om.BuildProject;
import com.versionone.om.BuildRun;
import com.versionone.om.ChangeSet;
import com.versionone.om.Story;

public class ChangeSetTester extends BaseSDKTester {

    private final static String name = "Test ChangeSet";
    private final static String reference = "123456";

    @Test
    public void testCreate() {
        ChangeSet changeSet = getInstance().create().changeSet(name,
                reference);

        Assert.assertEquals(name, changeSet.getName());
        Assert.assertEquals(reference, changeSet.getReference());
    }

    @Test
    public void testCreateChangeSetWithAttributes() {
        final String description = "Test for ChangeSet creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        ChangeSet changeSet = getInstance().create().changeSet(name, reference, attributes);

        Assert.assertEquals(description, changeSet.getDescription());

        changeSet.delete();
    }

    @Test
    public void testDelete() {
        ChangeSet changeSet = getInstance().create().changeSet(name,
                reference);

        AssetID id = changeSet.getID();

        changeSet.delete();

        resetInstance();

        Assert.assertNull(getInstance().get().changeSetByID(id));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClose() {
        ChangeSet changeSet = getInstance().create().changeSet(name,
                reference);
        Assert.assertFalse(changeSet.canClose());
        changeSet.close();
    }

    @Test
    public void testGetBuildRuns() {
        final String otherChangeSet = "Other ChangeSet";
        final String abcd = "abcd";
        final String br = "BR";
        final String notMyBr = "Not My BR";

        ChangeSet changeSet = getInstance().create().changeSet(name,
                reference);
        ChangeSet notMyChangeSet = getInstance().create().changeSet(
                otherChangeSet, abcd);
        BuildProject buildProject = getInstance().create().buildProject(
                "BP", "1234");
        BuildRun buildRun = buildProject.createBuildRun(br, DateTime.now());
        BuildRun notMyBuildRun = buildProject.createBuildRun(notMyBr, DateTime
                .now());
        buildRun.getChangeSets().add(changeSet);
        notMyBuildRun.getChangeSets().add(notMyChangeSet);
        AssetID changeSetId = changeSet.getID();
        AssetID buildRunId = buildRun.getID();

        resetInstance();

        changeSet = getInstance().get().changeSetByID(changeSetId);
        Collection<BuildRun> buildRuns = changeSet.getBuildRuns(null);
        Assert.assertEquals(1, buildRuns.size());

        Iterator<BuildRun> iterator = buildRuns.iterator();
        Assert.assertEquals(buildRunId, iterator.next().getID());
    }

    @Test
    public void testPrimaryWorkitems() {
        final String testStory = "Test Story";

        ChangeSet changeSet = getInstance().create().changeSet(name,
                reference);
        Story story = getSandboxProject().createStory(testStory);
        changeSet.getPrimaryWorkitems().add(story);
        AssetID changeSetId = changeSet.getID();

        resetInstance();

        changeSet = getInstance().get().changeSetByID(changeSetId);
        Assert.assertEquals(1, changeSet.getPrimaryWorkitems().size());
    }
}
