/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Attachment;
import com.versionone.om.Defect;
import com.versionone.om.Effort;
import com.versionone.om.Entity;
import com.versionone.om.Epic;
import com.versionone.om.Issue;
import com.versionone.om.Note;
import com.versionone.om.PrimaryWorkitem;
import com.versionone.om.Project;
import com.versionone.om.ProjectAsset;
import com.versionone.om.Request;
import com.versionone.om.Story;
import com.versionone.om.Task;
import com.versionone.om.TesterBase;
import com.versionone.om.filters.EntityFilter;

public class EntityFilterTester extends TesterBase {
    public Class<? extends Entity> entityType;
    EntityFilter filterMock = new EntityFilter() {
        public Class<? extends Entity> getEntityType() {
            return entityType;
        }
    };

    private String resolvePropertyName(Class<? extends Entity> clazz, String propertyName) throws Exception {
        entityType = clazz;
        return (String) invokePrivateMethod(EntityFilter.class, filterMock, "resolvePropertyName",
                new Class[]{String.class}, propertyName);
    }

    @Test
    public void testNoteAnnotations() throws Exception {
        Assert.assertEquals("Category", resolvePropertyName(Note.class, "type"));
    }

    @Test
    public void testDefectAnnotations() throws Exception {
        Assert.assertEquals("Resolution", resolvePropertyName(Defect.class, "resolutionDetails"));
        Assert.assertEquals("FixedInBuild", resolvePropertyName(Defect.class, "resolvedInBuild"));
        Assert.assertEquals("FoundInBuildRuns", resolvePropertyName(Defect.class, "foundIn"));
        Assert.assertEquals("VersionAffected", resolvePropertyName(Defect.class, "foundInVersion"));
    }

    @Test
    public void testIssueAnnotations() throws Exception {
        Assert.assertEquals("Order", resolvePropertyName(Issue.class, "rankOrder"));
    }

    @Test
    public void testProjectAssetAnnotations() throws Exception {
        Assert.assertEquals("Scope", resolvePropertyName(ProjectAsset.class, "project"));
        Assert.assertEquals("Number", resolvePropertyName(ProjectAsset.class, "displayID"));
    }

    @Test
    public void testTaskAnnotations() throws Exception {
        Assert.assertEquals("Category", resolvePropertyName(Task.class, "type"));
        Assert.assertEquals("Order", resolvePropertyName(Task.class, "rankOrder"));
        Assert.assertEquals("LastVersion", resolvePropertyName(Task.class, "build"));
    }

    @Test
    public void testTestAnnotations() throws Exception {
        Assert.assertEquals("Category", resolvePropertyName(com.versionone.om.Test.class, "type"));
        Assert.assertEquals("Order", resolvePropertyName(com.versionone.om.Test.class, "rankOrder"));
    }

    @Test
    public void testRequestAnnotations() throws Exception {
        Assert.assertEquals("Order", resolvePropertyName(Request.class, "rankOrder"));
    }

    @Test
    public void testEpicAnnotations() throws Exception {
        Assert.assertEquals("Category", resolvePropertyName(Epic.class, "type"));
        Assert.assertEquals("Parent", resolvePropertyName(Epic.class, "Parent"));
        Assert.assertEquals("Super", resolvePropertyName(Epic.class, "Super"));
        Assert.assertEquals("Order", resolvePropertyName(Epic.class, "RankOrder"));
    }

    @Test
    public void testPrimaryWorkitemAnnotations() throws Exception {
        Assert.assertEquals("Order", resolvePropertyName(PrimaryWorkitem.class, "rankOrder"));
        Assert.assertEquals("Parent", resolvePropertyName(PrimaryWorkitem.class, "theme"));
        Assert.assertEquals("CompletedInBuildRuns", resolvePropertyName(PrimaryWorkitem.class, "completedIn"));
        Assert.assertEquals("Timebox", resolvePropertyName(PrimaryWorkitem.class, "iteration"));
    }

    @Test
    public void testProjectAnnotations() throws Exception {
        Assert.assertEquals("Parent", resolvePropertyName(Project.class, "parentProject"));
        Assert.assertEquals("Schedule", resolvePropertyName(Project.class, "Schedule"));
    }

    @Test
    public void testAttachmentAnnotations() throws Exception {
        Assert.assertEquals("Category", resolvePropertyName(Attachment.class, "type"));
    }

    @Test
    public void testStoryAnnotations() throws Exception {
        Assert.assertEquals("Category", resolvePropertyName(Story.class, "type"));
        Assert.assertEquals("LastVersion", resolvePropertyName(Story.class, "build"));
        Assert.assertEquals("Super", resolvePropertyName(Story.class, "epic"));
    }

    @Test
    public void testEffortAnnotations() throws Exception {
        Assert.assertEquals("Scope", resolvePropertyName(Effort.class, "project"));
        Assert.assertEquals("Timebox", resolvePropertyName(Effort.class, "iteration"));
    }
}
