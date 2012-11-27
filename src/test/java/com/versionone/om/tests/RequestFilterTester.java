/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Member;
import com.versionone.om.Request;
import com.versionone.om.filters.RequestFilter;

public class RequestFilterTester extends BaseSDKTester {

    private Member getAnOwner() {
        for (Member member : getSandboxProject().getAssignedMembers()) {
            return member;
        }

        return null;
    }

    @Test
    public void testOwner() {
        Request expected = getInstance().create().request("Has Owner",
                getSandboxProject());
        Request not = getInstance().create().request("No Owner",
                getSandboxProject());

        Member owner = getAnOwner();
        expected.setOwner(owner);
        expected.save();

        testOwner(expected, not, owner);
    }

    @Test
    public void testNoOwner() {
        Request has = getInstance().create().request("Has Owner",
                getSandboxProject());
        Request not = getInstance().create().request("No Owner",
                getSandboxProject());

        Member owner = getAnOwner();
        has.setOwner(owner);
        has.save();

        testOwner(not, has, null);
    }

    void testOwner(Request expected, Request not, Member expectedOwner) {
        RequestFilter filter = new RequestFilter();
        filter.project.add(getSandboxProject());
        filter.owner.add(expectedOwner);

        resetInstance();
        expectedOwner = (expectedOwner != null) ? getInstance().get()
                .memberByID(expectedOwner.getID()) : null;
        expected = getInstance().get().requestByID(expected.getID());
        not = getInstance().get().requestByID(not.getID());

        Collection<Request> results = getSandboxProject().getRequests(filter);

        Assert.assertTrue("Expected to find request that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find request that doesn't match filter.",
                findRelated(not, results));
        for (Request result : results) {
            Assert.assertEquals(expectedOwner, result.getOwner());
        }
    }

    @Test
    public void testSource() {
        Request expected = getInstance().create().request("Has Source",
                getSandboxProject());
        Request not = getInstance().create().request("No Source",
                getSandboxProject());

        String expectedSource = expected.getSource().getAllValues()[0];
        expected.getSource().setCurrentValue(expectedSource);
        expected.save();

        testSource(expected, not, expectedSource);
    }

    @Test
    public void testNoSource() {
        Request expected = getInstance().create().request("Has Source",
                getSandboxProject());
        Request not = getInstance().create().request("No Source",
                getSandboxProject());

        String expectedSource = expected.getSource().getAllValues()[0];
        expected.getSource().setCurrentValue(expectedSource);
        expected.save();

        testSource(not, expected, null);
    }

    void testSource(Request expected, Request not, String expectedSource) {
        RequestFilter filter = new RequestFilter();
        filter.project.add(getSandboxProject());
        filter.source.add(expectedSource);

        resetInstance();
        expected = getInstance().get().requestByID(expected.getID());
        not = getInstance().get().requestByID(not.getID());

        Collection<Request> results = getSandboxProject().getRequests(filter);

        Assert.assertTrue("Expected to find request that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find request that doesn't match filter.",
                findRelated(not, results));
        for (Request result : results) {
            Assert.assertEquals(expectedSource, result.getSource()
                    .getCurrentValue());
        }
    }

    @Test
    public void testType() {
        Request expected = getInstance().create().request("Has Type",
                getSandboxProject());
        Request not = getInstance().create().request("No Type",
                getSandboxProject());

        String expectedType = expected.getType().getAllValues()[0];
        expected.getType().setCurrentValue(expectedType);
        expected.save();

        testType(expected, not, expectedType);
    }

    @Test
    public void testNoType() {
        Request expected = getInstance().create().request("Has Type",
                getSandboxProject());
        Request not = getInstance().create().request("No Type",
                getSandboxProject());

        String expectedType = expected.getType().getAllValues()[0];
        expected.getType().setCurrentValue(expectedType);
        expected.save();

        testType(not, expected, null);
    }

    void testType(Request expected, Request not, String expectedType) {
        RequestFilter filter = new RequestFilter();
        filter.project.add(getSandboxProject());
        filter.type.add(expectedType);

        resetInstance();
        expected = getInstance().get().requestByID(expected.getID());
        not = getInstance().get().requestByID(not.getID());

        Collection<Request> results = getSandboxProject().getRequests(filter);

        Assert.assertTrue("Expected to find request that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find request that doesn't match filter.",
                findRelated(not, results));
        for (Request result : results) {
            Assert.assertEquals(expectedType, result.getType()
                    .getCurrentValue());
        }
    }

    @Test
    public void testStatus() {
        Request expected = getInstance().create().request("Has Status",
                getSandboxProject());
        Request not = getInstance().create().request("No Status",
                getSandboxProject());

        String expectedStatus = expected.getStatus().getAllValues()[0];
        expected.getStatus().setCurrentValue(expectedStatus);
        expected.save();

        testStatus(expected, not, expectedStatus);
    }

    @Test
    public void testNoStatus() {
        Request expected = getInstance().create().request("Has Status",
                getSandboxProject());
        Request not = getInstance().create().request("No Status",
                getSandboxProject());

        String expectedStatus = expected.getStatus().getAllValues()[0];
        expected.getStatus().setCurrentValue(expectedStatus);
        expected.save();

        testStatus(not, expected, null);
    }

    void testStatus(Request expected, Request not, String expectedStatus) {
        RequestFilter filter = new RequestFilter();
        filter.project.add(getSandboxProject());
        filter.status.add(expectedStatus);

        resetInstance();
        expected = getInstance().get().requestByID(expected.getID());
        not = getInstance().get().requestByID(not.getID());

        Collection<Request> results = getSandboxProject().getRequests(filter);

        Assert.assertTrue("Expected to find request that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find request that doesn't match filter.",
                findRelated(not, results));
        for (Request result : results) {
            Assert.assertEquals(expectedStatus, result.getStatus()
                    .getCurrentValue());
        }
    }

    @Test
    public void testPriority() {
        Request expected = getInstance().create().request(
                "Has Priority", getSandboxProject());
        Request not = getInstance().create().request("No Priority",
                getSandboxProject());

        String expectedPriority = expected.getPriority().getAllValues()[0];
        expected.getPriority().setCurrentValue(expectedPriority);
        expected.save();

        testPriority(expected, not, expectedPriority);
    }

    @Test
    public void testNoPriority() {
        Request expected = getInstance().create().request(
                "Has Priority", getSandboxProject());
        Request not = getInstance().create().request("No Priority",
                getSandboxProject());

        String expectedPriority = expected.getPriority().getAllValues()[0];
        expected.getPriority().setCurrentValue(expectedPriority);
        expected.save();

        testPriority(not, expected, null);
    }

    void testPriority(Request expected, Request not, String expectedPriority) {
        RequestFilter filter = new RequestFilter();
        filter.project.add(getSandboxProject());
        filter.priority.add(expectedPriority);

        resetInstance();
        expected = getInstance().get().requestByID(expected.getID());
        not = getInstance().get().requestByID(not.getID());

        Collection<Request> results = getSandboxProject().getRequests(filter);

        Assert.assertTrue("Expected to find request that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find request that doesn't match filter.",
                findRelated(not, results));
        for (Request result : results) {
            Assert.assertEquals(expectedPriority, result.getPriority()
                    .getCurrentValue());
        }
    }

    @Test
    public void testRequestedBy() {
        Request expected = getInstance().create().request(
                "Has RequestedBy", getSandboxProject());
        Request not = getInstance().create().request("No RequestedBy",
                getSandboxProject());

        String expectedRequestedBy = "Me!!";
        expected.setRequestedBy(expectedRequestedBy);
        expected.save();

        testRequestedBy(expected, not, expectedRequestedBy);
    }

    @Test
    public void testNoRequestedBy() {
        Request expected = getInstance().create().request(
                "Has RequestedBy", getSandboxProject());
        Request not = getInstance().create().request("No RequestedBy",
                getSandboxProject());

        String expectedRequestedBy = "Me!!";
        expected.setRequestedBy(expectedRequestedBy);
        expected.save();

        testRequestedBy(not, expected, null);
    }

    void testRequestedBy(Request expected, Request not,
            String expectedRequestedBy) {
        RequestFilter filter = new RequestFilter();
        filter.project.add(getSandboxProject());
        filter.requestedBy.add(expectedRequestedBy);

        resetInstance();
        expected = getInstance().get().requestByID(expected.getID());
        not = getInstance().get().requestByID(not.getID());

        Collection<Request> results = getSandboxProject().getRequests(filter);

        Assert.assertTrue("Expected to find request that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find request that doesn't match filter.",
                findRelated(not, results));
        for (Request result : results) {
            Assert.assertEquals(expectedRequestedBy, result.getRequestedBy());
        }
    }

    @Test
    public void testReference() {
        Request expected = getInstance().create().request(
                "Has Reference", getSandboxProject());
        Request not = getInstance().create().request("No Reference",
                getSandboxProject());

        String expectedReference = "Me!!";
        expected.setReference(expectedReference);
        expected.save();

        testReference(expected, not, expectedReference);
    }

    @Test
    public void testNoReference() {
        Request expected = getInstance().create().request(
                "Has Reference", getSandboxProject());
        Request not = getInstance().create().request("No Reference",
                getSandboxProject());

        String expectedReference = "Me!!";
        expected.setReference(expectedReference);
        expected.save();

        testReference(not, expected, null);
    }

    void testReference(Request expected, Request not, String expectedReference) {
        RequestFilter filter = new RequestFilter();
        filter.project.add(getSandboxProject());
        filter.reference.add(expectedReference);

        resetInstance();
        expected = getInstance().get().requestByID(expected.getID());
        not = getInstance().get().requestByID(not.getID());

        Collection<Request> results = getSandboxProject().getRequests(filter);

        Assert.assertTrue("Expected to find request that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find request that doesn't match filter.",
                findRelated(not, results));
        for (Request result : results) {
            Assert.assertEquals(expectedReference, result.getReference());
        }
    }

    @Test
    public void testResolutionReason() {
        Request expected = getInstance().create().request(
                "Has ResolutionReason", getSandboxProject());
        Request not = getInstance().create().request(
                "No ResolutionReason", getSandboxProject());

        String expectedResolutionReason = expected.getResolutionReason()
                .getAllValues()[0];
        expected.getResolutionReason()
                .setCurrentValue(expectedResolutionReason);
        expected.save();

        testResolutionReason(expected, not, expectedResolutionReason);
    }

    @Test
    public void testNoResolutionReason() {
        Request expected = getInstance().create().request(
                "Has ResolutionReason", getSandboxProject());
        Request not = getInstance().create().request(
                "No ResolutionReason", getSandboxProject());

        String expectedResolutionReason = expected.getResolutionReason()
                .getAllValues()[0];
        expected.getResolutionReason()
                .setCurrentValue(expectedResolutionReason);
        expected.save();

        testResolutionReason(not, expected, null);
    }

    void testResolutionReason(Request expected, Request not,
            String expectedResolutionReason) {
        RequestFilter filter = new RequestFilter();
        filter.project.add(getSandboxProject());
        filter.resolutionReason.add(expectedResolutionReason);

        resetInstance();
        expected = getInstance().get().requestByID(expected.getID());
        not = getInstance().get().requestByID(not.getID());

        Collection<Request> results = getSandboxProject().getRequests(filter);

        Assert.assertTrue("Expected to find request that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                "Expected to NOT find request that doesn't match filter.",
                findRelated(not, results));
        for (Request result : results) {
            Assert.assertEquals(expectedResolutionReason, result
                    .getResolutionReason().getCurrentValue());
        }
    }

    @Test
    public void testRankOrder()
    {
            RequestFilter filter = new RequestFilter();
            filter.orderBy.add("RankOrder");
            Assert.assertTrue(-1 < getInstance().get().requests(filter).size());
    }

}
