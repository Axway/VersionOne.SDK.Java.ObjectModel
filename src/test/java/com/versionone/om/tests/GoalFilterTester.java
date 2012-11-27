/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Goal;
import com.versionone.om.filters.GoalFilter;

public class GoalFilterTester extends BaseSDKTester {

    @Test
    public void testType() {
        Goal expected = getInstance().create().goal("Has Type",
                getSandboxProject());
        Goal not = getInstance().create().goal("No Type",
                getSandboxProject());

        String expectedType = expected.getType().getAllValues()[0];
        expected.getType().setCurrentValue(expectedType);
        expected.save();

        testType(expected, not, expectedType);
    }

    @Test
    public void testNoType() {
        Goal expected = getInstance().create().goal("Has Type",
                getSandboxProject());
        Goal not = getInstance().create().goal("No Type",
                getSandboxProject());

        String expectedType = expected.getType().getAllValues()[0];
        expected.getType().setCurrentValue(expectedType);
        expected.save();

        testType(not, expected, null);
    }

    void testType(Goal expected, Goal not, String expectedType) {
        GoalFilter filter = new GoalFilter();
        filter.project.add(getSandboxProject());
        filter.type.add(expectedType);

        resetInstance();
        expected = getInstance().get().goalByID(expected.getID());
        not = getInstance().get().goalByID(not.getID());

        Collection<Goal> results = getSandboxProject().getGoals(filter);

        Assert.assertTrue("Expected to find goal that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find goal that doesn't match filter.",
                findRelated(not, results));
        for (Goal result : results) {
            Assert.assertEquals(expectedType, result.getType()
                    .getCurrentValue());
        }
    }

    @Test
    public void testPriority() {
        Goal expected = getInstance().create().goal("Has Priority",
                getSandboxProject());
        Goal not = getInstance().create().goal("No Priority",
                getSandboxProject());

        String expectedPriority = expected.getPriority().getAllValues()[0];
        expected.getPriority().setCurrentValue(expectedPriority);
        expected.save();

        testPriority(expected, not, expectedPriority);
    }

    @Test
    public void testNoPriority() {
        Goal expected = getInstance().create().goal("Has Priority",
                getSandboxProject());
        Goal not = getInstance().create().goal("No Priority",
                getSandboxProject());

        String expectedPriority = expected.getPriority().getAllValues()[0];
        expected.getPriority().setCurrentValue(expectedPriority);
        expected.save();

        testPriority(not, expected, null);
    }

    void testPriority(Goal expected, Goal not, String expectedPriority) {
        GoalFilter filter = new GoalFilter();
        filter.project.add(getSandboxProject());
        filter.priority.add(expectedPriority);

        resetInstance();
        expected = getInstance().get().goalByID(expected.getID());
        not = getInstance().get().goalByID(not.getID());

        Collection<Goal> results = getSandboxProject().getGoals(filter);

        Assert.assertTrue("Expected to find goal that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find goal that doesn't match filter.",
                findRelated(not, results));
        for (Goal result : results) {
            Assert.assertEquals(expectedPriority, result.getPriority()
                    .getCurrentValue());
        }
    }

}
