/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.versionone.DB;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.AssetID;
import com.versionone.om.Defect;
import com.versionone.om.Epic;
import com.versionone.om.IListValueProperty;
import com.versionone.om.Issue;
import com.versionone.om.Member;
import com.versionone.om.PrimaryWorkitem;
import com.versionone.om.Project;
import com.versionone.om.Request;
import com.versionone.om.Story;

public class RequestTester extends BaseSDKTester {

    @Test
    public void testCreateAndRetrieveRequest() {
        final String requestName = "New Name";

        final Project project = getInstance().get().projectByID(SCOPE_ZERO);
        AssetID id = getInstance().create().request(requestName, project).getID();

        resetInstance();

        Request request = getInstance().get().requestByID(id);

        Assert.assertEquals(request.getName(), requestName);
    }

    @Test
    public void testCreateRequestWithAttributes() {
        final String name = "TestRequestName";
        final Project project = getInstance().get().projectByID(SCOPE_ZERO);
        final String description = "Test for Request creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        Request request = getInstance().create().request(name, project, attributes);

        Assert.assertEquals(name, request.getName());
        Assert.assertEquals(description, request.getDescription());

        request.delete();
    }

    @Test
    public void testRequestOrder() {
        Project project = getInstance().get().projectByID(SCOPE_ZERO);
        Request request1 = project.createRequest("Request 1");
        Request request2 = project.createRequest("Request 2");

        AssetID id1 = request1.getID();
        AssetID id2 = request2.getID();

        request1.getRankOrder().setBelow(request2);

        Assert.assertTrue(request1.getRankOrder().isBelow(request2));
        Assert.assertTrue(request2.getRankOrder().isAbove(request1));

        resetInstance();

        request1 = getInstance().get().requestByID(id1);
        request2 = getInstance().get().requestByID(id2);

        request1.getRankOrder().setAbove(request2);

        Assert.assertTrue(request1.getRankOrder().isAbove(request2));
        Assert.assertTrue(request2.getRankOrder().isBelow(request1));
    }

    /**
     * <p>Not exist in .NET
     */
    @Test
    public void testRequestAtributes() {
        final String requestedBy = "United Nations Organization";
        final String reference = "Kofi Annan";
        final String resolutionDetails = "Sample details";
        final String sourceValue = "Customer";
        final String typeValue = "Feature";
        final String priorityValue = "High";
        final String resolutionReasonValue = "Rejected";
        final String statusValue = "Reviewed";

        final Project project = getSandboxProject();
        Request request = project.createRequest("testOwner");
        final Member member = getSandboxMember();
        request.setOwner(member);
        request.setRequestedBy(requestedBy);
        request.setReference(reference);
        request.setResolutionDetails(resolutionDetails);

        //set current values
        IListValueProperty source = request.getSource();
        source.setCurrentValue(sourceValue);
        IListValueProperty type = request.getType();
        type.setCurrentValue(typeValue);
        IListValueProperty priority = request.getPriority();
        priority.setCurrentValue(priorityValue);
        IListValueProperty resolutionReason = request.getResolutionReason();
        resolutionReason.setCurrentValue(resolutionReasonValue);
        IListValueProperty status = request.getStatus();
        status.setCurrentValue(statusValue);

        request.save();
        final AssetID id = request.getID();

        resetInstance();

        request = getInstance().get().requestByID(id);
        Assert.assertEquals(member, request.getOwner());
        Assert.assertEquals(requestedBy, request.getRequestedBy());
        Assert.assertEquals(reference, request.getReference());
        Assert.assertEquals(resolutionDetails, request.getResolutionDetails());

        source = request.getSource();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(source.getAllValues()),
                "Product Mgt", "Sales", "Customer", "Development");
        Assert.assertEquals(sourceValue, source.getCurrentValue());

        type = request.getType();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(type.getAllValues()),
                "Feature", "Defect", "Question");
        Assert.assertEquals(typeValue, type.getCurrentValue());

        priority = request.getPriority();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(priority.getAllValues()),
                "Low", "Medium", "High");
        Assert.assertEquals(priorityValue, priority.getCurrentValue());

        resolutionReason = request.getResolutionReason();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(resolutionReason.getAllValues()),
                "Implemented", "Rejected");
        Assert.assertEquals(resolutionReasonValue, resolutionReason.getCurrentValue());

        status = request.getStatus();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(status.getAllValues()),
                "Reviewed", "Approved", "Rejected");
        Assert.assertEquals(statusValue, status.getCurrentValue());

        // created request doesn't have any primary workitems
        Collection<PrimaryWorkitem> workitems = request.getPrimaryWorkitems(null);
        Assert.assertEquals(0, workitems.size());

        // created request doesn't have any issues
        Collection<Issue> issue = request.getIssues();
        Assert.assertEquals(0, issue.size());

        // created request doesn't have any epics
        Collection<Epic> epics = request.getEpics(null);
        Assert.assertEquals(0, epics.size());

        Assert.assertTrue(request.canClose());
        Assert.assertFalse(request.canReactivate());

        Assert.assertTrue(request.isActive());
        Assert.assertFalse(request.isClosed());

        //close request
        request.close();
        request.save();

        resetInstance();

        request = getInstance().get().requestByID(id);

        Assert.assertFalse(request.isActive());
        Assert.assertTrue(request.isClosed());

        Assert.assertFalse(request.canClose());
        Assert.assertTrue(request.canReactivate());

        // reactivate request
        request.reactivate();
        request.save();

        resetInstance();

        request = getInstance().get().requestByID(id);

        Assert.assertTrue(request.isActive());
        Assert.assertFalse(request.isClosed());

        Assert.assertTrue(request.isActive());
        Assert.assertFalse(request.isClosed());

        Assert.assertTrue(request.canDelete());

        // delete request
        request.delete();

        resetInstance();

        request = getInstance().get().requestByID(id);

        Assert.assertNull(request);

        //Clean
        project.delete();
    }

    /**
     * <p>Not exist in .NET
     */
    @Test
    public void testRequestGenerator() {
        Project project = getSandboxProject();
        Request request = project.createRequest("testRequest");
        request.save();
        AssetID id = request.getID();
        resetInstance();

        request = getInstance().get().requestByID(id);

        Defect defect = request.generateDefect();
        Assert.assertNotNull(defect);

        Story story = request.generateStory();
        Assert.assertNotNull(story);

        //Clean
        story.delete();
        defect.delete();
        request.delete();
        project.delete();
    }
}
