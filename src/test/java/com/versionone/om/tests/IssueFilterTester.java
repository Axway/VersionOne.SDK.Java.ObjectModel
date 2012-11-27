/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Issue;
import com.versionone.om.Member;
import com.versionone.om.filters.IssueFilter;

public class IssueFilterTester extends BaseSDKTester {


    @Test
    public void testType() {
        Issue expected = getInstance().create().issue("Has Type",
                getSandboxProject());
        Issue not = getInstance().create().issue("No Type",
                getSandboxProject());

        String expectedType = expected.getType().getAllValues()[0];
        expected.getType().setCurrentValue(expectedType);
        expected.save();

        testType(expected, not, expectedType);
    }

    @Test
    public void testNoType() {
        Issue expected = getInstance().create().issue("Has Type",
                getSandboxProject());
        Issue not = getInstance().create().issue("No Type",
                getSandboxProject());

        String expectedType = expected.getType().getAllValues()[0];
        expected.getType().setCurrentValue(expectedType);
        expected.save();

        testType(not, expected, null);
    }

    void testType(Issue expected, Issue not, String expectedType) {
        IssueFilter filter = new IssueFilter();
        filter.project.add(getSandboxProject());
        filter.type.add(expectedType);

        resetInstance();
        expected = getInstance().get().issueByID(expected.getID());
        not = getInstance().get().issueByID(not.getID());

        Collection<Issue> results = getSandboxProject().getIssues(filter);

        Assert.assertTrue("Expected to find Issue that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find Issue that doesn't match filter.",
                findRelated(not, results));
        for (Issue result : results) {
            Assert.assertEquals(expectedType, result.getType()
                    .getCurrentValue());
        }
    }

    @Test
    public void testPriority() {
        Issue expected = getInstance().create().issue("Has Priority",
                getSandboxProject());
        Issue not = getInstance().create().issue("No Priority",
                getSandboxProject());

        String expectedPriority = expected.getPriority().getAllValues()[0];
        expected.getPriority().setCurrentValue(expectedPriority);
        expected.save();

        testPriority(expected, not, expectedPriority);
    }

    @Test
    public void testNoPriority() {
        Issue expected = getInstance().create().issue("Has Priority",
                getSandboxProject());
        Issue not = getInstance().create().issue("No Priority",
                getSandboxProject());

        String expectedPriority = expected.getPriority().getAllValues()[0];
        expected.getPriority().setCurrentValue(expectedPriority);
        expected.save();

        testPriority(not, expected, null);
    }

    void testPriority(Issue expected, Issue not, String expectedPriority) {
        IssueFilter filter = new IssueFilter();
        filter.project.add(getSandboxProject());
        filter.priority.add(expectedPriority);

        resetInstance();
        expected = getInstance().get().issueByID(expected.getID());
        not = getInstance().get().issueByID(not.getID());

        Collection<Issue> results = getSandboxProject().getIssues(filter);

        Assert.assertTrue("Expected to find Issue that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find Issue that doesn't match filter.",
                findRelated(not, results));
        for (Issue result : results) {
            Assert.assertEquals(expectedPriority, result.getPriority()
                    .getCurrentValue());
        }
    }

    @Test
    public void testResolutionReason() {
        Issue expected = getInstance().create().issue(
                "Has ResolutionReason", getSandboxProject());
        Issue not = getInstance().create().issue("No ResolutionReason",
                getSandboxProject());

        String expectedResolutionReason = expected.getResolutionReason()
                .getAllValues()[0];
        expected.getResolutionReason()
                .setCurrentValue(expectedResolutionReason);
        expected.save();

        testResolutionReason(expected, not, expectedResolutionReason);
    }

    @Test
    public void testNoResolutionReason() {
        Issue expected = getInstance().create().issue(
                "Has ResolutionReason", getSandboxProject());
        Issue not = getInstance().create().issue("No ResolutionReason",
                getSandboxProject());

        String expectedResolutionReason = expected.getResolutionReason()
                .getAllValues()[0];
        expected.getResolutionReason()
                .setCurrentValue(expectedResolutionReason);
        expected.save();

        testResolutionReason(not, expected, null);
    }

    void testResolutionReason(Issue expected, Issue not,
            String expectedResolutionReason) {
        IssueFilter filter = new IssueFilter();
        filter.project.add(getSandboxProject());
        filter.resolutionReason.add(expectedResolutionReason);

        resetInstance();
        expected = getInstance().get().issueByID(expected.getID());
        not = getInstance().get().issueByID(not.getID());

        Collection<Issue> results = getSandboxProject().getIssues(filter);

        Assert.assertTrue("Expected to find Issue that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find Issue that doesn't match filter.",
                findRelated(not, results));
        for (Issue result : results) {
            Assert.assertEquals(expectedResolutionReason, result
                    .getResolutionReason().getCurrentValue());
        }
    }

    @Test
    public void testOwner() {
        Issue issue = getSandboxProject().createIssue("Issue Owned By Andre");
        Assert.assertNull(issue.getOwner());
        Member andre = getInstance().get().memberByID("Member:1000");
        issue.setOwner(andre);
        issue.save();
        IssueFilter filter = new IssueFilter();
        filter.owner.add(andre);
        filter.project.add(getSandboxProject());
        Assert.assertEquals(1, getInstance().get().issues(filter).size());
    }

    @Test
    public void testRankOrder() {
        IssueFilter filter = new IssueFilter();
        filter.orderBy.add("RankOrder");
        int size = getInstance().get().issues(filter).size();
        Assert.assertTrue(-1 < size);
    }


}
