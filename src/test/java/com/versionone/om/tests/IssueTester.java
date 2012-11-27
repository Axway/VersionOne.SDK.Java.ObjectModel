/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;


public class IssueTester extends BaseSDKTester {

    @Override
    protected Project createSandboxProject(Project rootProject) {
        Map<String, Object> mandatoryAttributes = new HashMap<String, Object>(1);
        mandatoryAttributes.put("Scheme", getDefaultSchemeOid());

        return getEntityFactory().createProject(getSandboxName(), rootProject, mandatoryAttributes);
    }

    @Test
    public void testCreateAndRetrieveIssue() {
        final String name = "New Name";

        AssetID id = getInstance().create().issue(name,
                getInstance().get().projectByID(SCOPE_ZERO)).getID();
        resetInstance();

        Issue issue = getInstance().get().issueByID(id);
        Assert.assertEquals(issue.getName(), name);
    }

    @Test
    public void testCreateIssueWithAttributes() {
        final String name = "IssueName";
        final String description = "Test for Issue creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        getInstance().setValidationEnabled(true);

        Issue issue = getInstance().create().issue(name, getSandboxProject(), attributes);

        resetInstance();

        issue = getInstance().get().issueByID(issue.getID());

        Assert.assertEquals(name, issue.getName());
        Assert.assertEquals(description, issue.getDescription());
        Assert.assertEquals(getSandboxProject(), issue.getProject());

        issue.delete();
        getInstance().setValidationEnabled(false);
    }


    @Test
    public void testIssueOrder() {
        Project project = getInstance().get().projectByID(SCOPE_ZERO);
        Issue issue1 = project.createIssue("Issue 1");
        Issue issue2 = project.createIssue("Issue 2");

        AssetID id1 = issue1.getID();
        AssetID id2 = issue2.getID();

        issue1.getRankOrder().setBelow(issue2);

        Assert.assertTrue(issue1.getRankOrder().isBelow(issue2));
        Assert.assertTrue(issue2.getRankOrder().isAbove(issue1));
        resetInstance();

        issue1 = getInstance().get().issueByID(id1);
        issue2 = getInstance().get().issueByID(id2);

        issue1.getRankOrder().setAbove(issue2);

        Assert.assertTrue(issue1.getRankOrder().isAbove(issue2));
        Assert.assertTrue(issue2.getRankOrder().isBelow(issue1));
    }

    @Test
    public void testOwner() {
        Issue issue = getSandboxProject().createIssue("Issue with no Owner");
        Assert.assertNull(issue.getOwner());
    }

    /**
     * <p>Not exist in .NET
     */
    @Test
    public void testAtributes() {
        final String resolutionDetails = "Sample details";
        final String identificator = "Identificator";
        final String reference = "Reference Stub";
        final String sourceValue = "Sales";
        final String typeValue = "Impediment";
        final String priorityValue = "Medium";
        final String resolutionReasonValue = "No Action";

        Issue issue = getSandboxProject().createIssue("Issue with no Owner");
        issue.setResolutionDetails(resolutionDetails);
        issue.setIdentifiedBy(identificator);
        issue.setReference(reference);
        final Team team = getSandboxTeam();
        issue.setTeam(team);
        issue.save();
        final AssetID id = issue.getID();
        resetInstance();

        issue = getInstance().get().issueByID(id);

        Assert.assertEquals(resolutionDetails, issue.getResolutionDetails());
        Assert.assertEquals(identificator, issue.getIdentifiedBy());
        Assert.assertEquals(reference, issue.getReference());
        Assert.assertEquals(team, issue.getTeam());

        // created issue doesn't have any workitems
        final Collection<PrimaryWorkitem> workitems = issue.getPrimaryWorkitems(null);
        Assert.assertEquals(0, workitems.size());

        // created issue doesn't have any requests
        final Collection<Request> requests = issue.getRequests(null);
        Assert.assertEquals(0, requests.size());

        // created issue doesn't have any blocked primary workitems
        final Collection<PrimaryWorkitem> blockedWorkitems =
                issue.getBlockedPrimaryWorkitems(null);
        Assert.assertEquals(0, blockedWorkitems.size());

        // created issue doesn't have any epics
        final Collection<Epic> epics = issue.getEpics(null);
        Assert.assertEquals(0, epics.size());

        IListValueProperty source = issue.getSource();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(source.getAllValues()),
                "Product Mgt", "Sales", "Customer", "Development");

        IListValueProperty type = issue.getType();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(type.getAllValues()),
                "Impediment");

        IListValueProperty priority = issue.getPriority();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(priority.getAllValues()),
                "Low", "Medium", "High");

        IListValueProperty resolutionReason = issue.getResolutionReason();
        ListAssert.areEqualIgnoringOrder(
                Arrays.asList(resolutionReason.getAllValues()),
                "Resolved", "No Action");

        Assert.assertTrue(issue.canClose());
        Assert.assertFalse(issue.canReactivate());

        Assert.assertTrue(issue.isActive());
        Assert.assertFalse(issue.isClosed());

        //set current values
        source.setCurrentValue(sourceValue);
        type.setCurrentValue(typeValue);
        priority.setCurrentValue(priorityValue);
        resolutionReason.setCurrentValue(resolutionReasonValue);

        //update issue
        issue.save();

        resetInstance();

        issue = getInstance().get().issueByID(id);

        source = issue.getSource();
        Assert.assertEquals(sourceValue, source.getCurrentValue());

        type = issue.getType();
        Assert.assertEquals(typeValue, type.getCurrentValue());

        priority = issue.getPriority();
        Assert.assertEquals(priorityValue, priority.getCurrentValue());

        resolutionReason = issue.getResolutionReason();
        Assert.assertEquals(resolutionReasonValue,
                resolutionReason.getCurrentValue());

        // close issue
        issue.close();
        issue.save();

        resetInstance();

        issue = getInstance().get().issueByID(id);

        Assert.assertFalse(issue.isActive());
        Assert.assertTrue(issue.isClosed());

        Assert.assertFalse(issue.canClose());
        Assert.assertTrue(issue.canReactivate());

        // reactivate issue
        issue.reactivate();
        issue.save();

        resetInstance();

        issue = getInstance().get().issueByID(id);

        Assert.assertTrue(issue.isActive());
        Assert.assertFalse(issue.isClosed());

        Assert.assertTrue(issue.canClose());
        Assert.assertFalse(issue.canReactivate());

        Assert.assertTrue(issue.canDelete());

        // delete issue
        issue.delete();

        resetInstance();

        issue = getInstance().get().issueByID(id);
        Assert.assertNull(issue);

        //clean
        team.delete();
    }

    /**
     * <p>Not exist in .NET
     */
    @Test
    public void testIssueGenerator() {
        Project project = getSandboxProject();
        Issue issue = project.createIssue("testIssue");
        issue.save();
        AssetID id = issue.getID();
        resetInstance();

        issue = getInstance().get().issueByID(id);

        Defect defect = issue.generateDefect();
        Assert.assertNotNull(defect);

        Story story = issue.generateStory();
        Assert.assertNotNull(story);

        //Clean
        story.delete();
        defect.delete();
        issue.delete();
        project.delete();
    }


    @Test
    public void testEpicsAssignedToCurrentIssue() {
        final Project project = getSandboxProject();
        final String issueName = "Issue name";
        final String epicName = "Epic Name";
                
        Issue issue = getEntityFactory().create(new EntityFactory.IEntityCreator<Issue>() {
            public Issue create() {
                return getInstance().create().issue(issueName, project);
            }
        });
        
        Epic epic = getEntityFactory().createEpic(epicName, project);
        epic.getIssues().add(issue);
        epic.save();

        resetInstance();

        Collection<Epic> epics = issue.getEpics(null);
        Assert.assertEquals(1, epics.size());
        Assert.assertEquals(epic, first(epics));
    }

    @Test
    public void testBlockedEpicsIssue() {
        final Project project = getSandboxProject();
        final String issueName = "Issue name";
        final String epicName = "Epic Name";

        Issue issue = getEntityFactory().create(new EntityFactory.IEntityCreator<Issue>() {
            public Issue create() {
                return getInstance().create().issue(issueName, project);
            }
        });

        Epic epic = getEntityFactory().createEpic(epicName, project);
        epic.getBlockingIssues().add(issue);
        epic.save();

        resetInstance();

        Collection<Epic> blockedEpics = issue.getBlockedEpics(null);
        Assert.assertEquals(1, blockedEpics.size());
        Assert.assertEquals(epic, first(blockedEpics));
    }
}
