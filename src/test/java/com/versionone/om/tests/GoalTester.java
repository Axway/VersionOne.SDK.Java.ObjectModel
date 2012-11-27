/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;

public class GoalTester extends BaseSDKTester {

    @Test
    public void testCreateAndRetrieveGoal() {
        final String name = "New Name";
        AssetID id = getInstance().create().goal(name,
                getInstance().get().projectByID(SCOPE_ZERO)).getID();
        resetInstance();

        Goal goal = getInstance().get().goalByID(id);
        Assert.assertEquals(goal.getName(), name);
    }

    @Test
    public void testCreateGoalWithAttributes() {
        final String name = "GoalName";
        final String description = "Test for Goal creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        getInstance().setValidationEnabled(true);

        Goal goal = getSandboxProject().createGoal(name, attributes);

        resetInstance();

        goal = getInstance().get().goalByID(goal.getID());

        Assert.assertEquals(name, goal.getName());
        Assert.assertEquals(description, goal.getDescription());
        Assert.assertEquals(getSandboxProject(), goal.getProject());

        goal.delete();
        getInstance().setValidationEnabled(false);
    }

    /**
     * <p>Not exist in .NET
     */
    @Test
    public void testGoalAttributes() {
        final String name = "New Name";
        final String priorityValue = "Medium";
        final String typeValue = "Market";

        final Project project = getSandboxProject();
        final Member customer = getSandboxMember();
        Goal theme = getInstance().create().goal(name, project);
        theme.save();
        final AssetID id = theme.getID();
        resetInstance();

        theme = getInstance().get().goalByID(id);

        IListValueProperty priority = theme.getPriority();
        priority.setCurrentValue(priorityValue);

        IListValueProperty type = theme.getType();
        type.setCurrentValue(typeValue);

        theme.save();

        resetInstance();

        theme = getInstance().get().goalByID(id);

        ListAssert.areEqualIgnoringOrder(Arrays.asList(type.getAllValues()),
                "Competitive","Financial","Market","Operations", "Strategic");
        ListAssert.areEqualIgnoringOrder(Arrays.asList(priority.getAllValues()),
                "Low", "Medium", "High");

        priority = theme.getPriority();
        type = theme.getType();
        Assert.assertEquals(priorityValue, priority.getCurrentValue());
        Assert.assertEquals(typeValue, type.getCurrentValue());

        final Collection<PrimaryWorkitem> workitems = theme.getPrimaryWorkitems(null);

        //created goal doesn't have any workitems
        Assert.assertEquals(0, workitems.size());

        final Collection<Attachment> attachments = theme.getAttachments(null);

        //created goal doesn't have any workitems
        Assert.assertEquals(0, attachments.size());

        // verify status goal
        Assert.assertTrue(theme.canClose());
        Assert.assertFalse(theme.canReactivate());
        Assert.assertTrue(theme.isActive());
        Assert.assertFalse(theme.isClosed());

        // close goal
        theme.close();
        theme.save();

        resetInstance();

        // get goal from web-server
        theme = getInstance().get().goalByID(id);

        // verify status
        Assert.assertFalse(theme.isActive());
        Assert.assertTrue(theme.isClosed());
        Assert.assertTrue(theme.canReactivate());

        // reactivate closed goal
        theme.reactivate();

        theme.save();

        resetInstance();

        // get goal from web-server
        theme = getInstance().get().goalByID(id);

        // verify status
        Assert.assertTrue(theme.isActive());
        Assert.assertFalse(theme.isClosed());
        Assert.assertFalse(theme.canReactivate());
        Assert.assertTrue(theme.canDelete());

        // delete goal
        theme.delete();

        resetInstance();

        theme = getInstance().get().goalByID(id);
        Assert.assertNull(theme);

        // delete all created data
        customer.delete();
        project.delete();
    }
}
