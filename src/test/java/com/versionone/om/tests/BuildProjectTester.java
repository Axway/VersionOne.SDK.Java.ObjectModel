/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.*;

import junit.framework.Assert;

import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.AssetID;
import com.versionone.om.BuildProject;
import com.versionone.om.BuildRun;
import com.versionone.om.filters.BuildRunFilter;

public class BuildProjectTester extends BaseSDKTester {

    private static final String VAR_B = "B";
    private static final String VAR_A = "A";
    private static final String PASSED = "Passed";
    private static final String DATE_2008_1_3 = "2008-1-3";
    private static final String DATE_2008_1_2 = "2008-1-2";
    private static final String DATE_2008_1_1 = "2008-1-1";
    private static final String RUN_3 = "Run 3";
    private static final String RUN_2 = "Run 2";
    private static final String RUN_1 = "Run 1";
    private static final String PROJECT = "Project";
    private static final String MY_PROJECT = "My Project";

    @Test
    public void testCreate() {
        BuildProject project = getInstance().create().buildProject(
                MY_PROJECT, PROJECT);

        Assert.assertEquals(MY_PROJECT, project.getName());
        Assert.assertEquals(PROJECT, project.getReference());
    }

    @Test
    public void testCreateBuildProjectWithAttributes() {
        final String description = "Test for BuildProject creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        BuildProject project = getInstance().create().buildProject(MY_PROJECT, PROJECT, attributes);

        Assert.assertEquals(description, project.getDescription());

        project.delete();
    }

    @Test
    public void testDeleteCascadeRuns() {
        BuildProject project = getInstance().create().buildProject(
                MY_PROJECT, "Project");

        List<AssetID> ids = new ArrayList<AssetID>(4);
        ids.add(project.getID());

        ids.add(project.createBuildRun(RUN_1, new DateTime(DATE_2008_1_1))
                .getID());
        ids.add(project.createBuildRun(RUN_2, new DateTime(DATE_2008_1_2))
                .getID());
        ids.add(project.createBuildRun(RUN_3, new DateTime(DATE_2008_1_3))
                .getID());

        project.delete();

        resetInstance();

        for (AssetID id : ids) {
            Assert.assertNull(getInstance().get().baseAssetByID(id));
        }
    }


    @Test
    public void testDelete() {
        BuildProject project = getInstance().create().buildProject(
                MY_PROJECT, PROJECT);

        AssetID id = project.getID();

        project.delete();

        resetInstance();

        Assert.assertNull(getInstance().get().buildProjectByID(id));
    }

    @Test
    public void testCloseCascadeRuns() {
        BuildProject project = getInstance().create().buildProject(
                MY_PROJECT, PROJECT);

        List<AssetID> ids = new ArrayList<AssetID>(4);
        ids.add(project.getID());
        ids.add(project.createBuildRun(RUN_1, new DateTime(DATE_2008_1_1))
                .getID());
        ids.add(project.createBuildRun(RUN_2, new DateTime(DATE_2008_1_2))
                .getID());
        ids.add(project.createBuildRun(RUN_3, new DateTime(DATE_2008_1_3))
                .getID());

        project.close();

        resetInstance();

        for (AssetID id : ids) {
            Assert.assertTrue(getInstance().get().baseAssetByID(id)
                    .isClosed());
        }
    }

    @Test
    public void testReactivateCascadeRuns() {
        BuildProject project = getInstance().create().buildProject(
                MY_PROJECT, PROJECT);

        List<AssetID> ids = new ArrayList<AssetID>(4);
        ids.add(project.getID());
        ids.add(project.createBuildRun(RUN_1, new DateTime(DATE_2008_1_1))
                .getID());
        ids.add(project.createBuildRun(RUN_2, new DateTime(DATE_2008_1_2))
                .getID());
        ids.add(project.createBuildRun(RUN_3, new DateTime(DATE_2008_1_3))
                .getID());

        project.close();

        for (AssetID id : ids) {
            Assert.assertTrue(getInstance().get().baseAssetByID(id)
                    .isClosed());
        }

        project.reactivate();

        for (AssetID id : ids) {
            Assert.assertTrue(getInstance().get().baseAssetByID(id)
                    .isActive());
        }
    }

    @Test
    public void testGetBuildRuns() {
        BuildProject project = getInstance().create().buildProject(
                MY_PROJECT, PROJECT);

        BuildRun run1 = project.createBuildRun(RUN_1, new DateTime(
                DATE_2008_1_1));
        BuildRun run2 = project.createBuildRun(RUN_2, new DateTime(
                DATE_2008_1_2));
        BuildRun run3 = project.createBuildRun(RUN_3, new DateTime(
                DATE_2008_1_3));

        run1.setReference(VAR_A);
        run2.setReference(VAR_A);
        run3.setReference(VAR_B);

        run1.getStatus().setCurrentValue(PASSED);
        run2.getStatus().setCurrentValue(PASSED);
        run3.getStatus().setCurrentValue("Failed");

        run1.save();
        run2.save();
        run3.save();

        BuildRunFilter filter = new BuildRunFilter();
        filter.references.add(VAR_A);
        filter.status.add(PASSED);
        Collection<BuildRun> runs = project.getBuildRuns(filter);
        ListAssert.areEqualIgnoringOrder(new Object[] { "Run 1", "Run 2" },
                runs, new EntityToNameTransformer<BuildRun>());
    }
}
