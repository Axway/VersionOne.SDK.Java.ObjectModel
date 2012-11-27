/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.Iteration;
import com.versionone.om.Member;
import com.versionone.om.Project;
import com.versionone.om.Retrospective;
import com.versionone.om.Team;
import com.versionone.om.filters.RetrospectiveFilter;

public class RetrospectiveFilterTester extends BaseSDKTester {

    @Override
    protected Project createSandboxProject(Project rootProject) {
    	Map<String, Object> attributes = new HashMap<String, Object>();
    	attributes.put("Scheme", getDefaultSchemeOid());
        return getInstance().create().project(super.getSandboxName(), rootProject, DateTime.now(), getSandboxSchedule(), attributes);
    }

    @Test
    public void testIteration() {
        Retrospective scheduled = getSandboxProject().createRetrospective(
                "Has Iteration");
        Retrospective notScheduled = getSandboxProject().createRetrospective(
                "No Iteration");

        Iteration iteration = getSandboxProject().createIteration();
        scheduled.setIteration(iteration);
        scheduled.save();

        testIteration(scheduled, notScheduled, iteration);
    }

    @Test
    public void testNoIteration() {
        Retrospective scheduled = getSandboxProject().createRetrospective(
                "Has Iteration");
        Retrospective notScheduled = getSandboxProject().createRetrospective(
                "No Iteration");

        Iteration iteration = getSandboxProject().createIteration();
        scheduled.setIteration(iteration);
        scheduled.save();

        testIteration(notScheduled, scheduled, null);
    }

    private void testIteration(Retrospective expected, Retrospective not,
            Iteration expectedIteration) {
        RetrospectiveFilter filter = new RetrospectiveFilter();
        filter.project.add(getSandboxProject());
        filter.iteration.add(expectedIteration);

        resetInstance();
        expectedIteration = (expectedIteration != null) ? getInstance()
                .get().iterationByID(expectedIteration.getID()) : null;
        expected = getInstance().get()
                .retrospectiveByID(expected.getID());
        not = getInstance().get().retrospectiveByID(not.getID());

        Collection<Retrospective> results = getSandboxProject()
                .getRetrospectives(filter);

        Assert.assertTrue(
                "Expected to find Retrospective that matched filter.",
                findRelated(expected, results));
        Assert
                .assertFalse(
                        "Expected to NOT find Retrospective that doesn't match filter.",
                        findRelated(not, results));
        for (Retrospective result : results) {
            Assert.assertEquals(expectedIteration, result.getIteration());
        }
    }

    @Test
    public void testFacilitatedBy() {
        Retrospective facilitated = getInstance().create()
                .retrospective("Has FacilitatedBy", getSandboxProject());
        Retrospective notFacilitated = getInstance().create()
                .retrospective("No FacilitatedBy", getSandboxProject());

        Member facilitator = getAFacilitator();
        facilitated.setFacilitatedBy(facilitator);
        facilitated.save();

        testFacilitatedBy(facilitated, notFacilitated, facilitator);
    }

    @Test
    public void testNoFacilitatedBy() {
        Retrospective facilitated = getInstance().create()
                .retrospective("Has FacilitatedBy", getSandboxProject());
        Retrospective notFacilitated = getInstance().create()
                .retrospective("No FacilitatedBy", getSandboxProject());

        Member facilitator = getAFacilitator();
        facilitated.setFacilitatedBy(facilitator);
        facilitated.save();

        testFacilitatedBy(notFacilitated, facilitated, null);
    }

    private Member getAFacilitator() {
        for (Member member : getSandboxProject().getAssignedMembers()) {
            return member;
        }

        return null;
    }

    private void testFacilitatedBy(Retrospective expected, Retrospective not,
            Member expectedFacilitator) {
        RetrospectiveFilter filter = new RetrospectiveFilter();
        filter.project.add(getSandboxProject());
        filter.facilitatedBy.add(expectedFacilitator);

        resetInstance();
        expectedFacilitator = (expectedFacilitator != null) ? getInstance()
                .get().memberByID(expectedFacilitator.getID()) : null;
        expected = getInstance().get()
                .retrospectiveByID(expected.getID());
        not = getInstance().get().retrospectiveByID(not.getID());

        Collection<Retrospective> results = getSandboxProject()
                .getRetrospectives(filter);

        Assert.assertTrue(
                "Expected to find Retrospective that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                        "Expected to NOT find Retrospective that doesn't match filter.",
                        findRelated(not, results));
        for (Retrospective result : results) {
            Assert.assertEquals(expectedFacilitator, result.getFacilitatedBy());
        }
    }

    @Test
    public void testDate() throws ParseException {
        Retrospective hasDate = getInstance().create().retrospective("Has Date", getSandboxProject());
        Retrospective noDate = getInstance().create().retrospective("No Date", getSandboxProject());

        DateTime expectedDate = DateTime.now();
        hasDate.setDate(expectedDate);
        expectedDate = expectedDate.getDate();  // round to nearest day, as we'd expect Mort to.
        hasDate.save();

        testDate(hasDate, noDate, expectedDate);
    }

    @Test
    public void testNoDate() {
        Retrospective hasDate = getInstance().create().retrospective("Has Date", getSandboxProject());
        Retrospective noDate = getInstance().create().retrospective("No Date", getSandboxProject());

        DateTime expectedDate = DateTime.now().getDate();
        hasDate.setDate(expectedDate);
        hasDate.save();

        testDate(noDate, hasDate, new DateTime(null));
    }

    private void testDate(Retrospective expected, Retrospective not, DateTime expectedDate) {
        RetrospectiveFilter filter = new RetrospectiveFilter();
        filter.project.add(getSandboxProject());
        filter.date.add(expectedDate);

        resetInstance();
        expected = getInstance().get()
                .retrospectiveByID(expected.getID());
        not = getInstance().get().retrospectiveByID(not.getID());

        Collection<Retrospective> results = getSandboxProject()
                .getRetrospectives(filter);

        Assert.assertTrue(
                "Expected to find Retrospective that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                        "Expected to NOT find Retrospective that doesn't match filter.",
                        findRelated(not, results));
        for (Retrospective result : results) {
            Assert.assertEquals(expectedDate, result.getDate());
        }
    }

    @Test
    public void testTeam() {
        Retrospective hasTeam = getSandboxProject().createRetrospective(
                "Has Team");
        Retrospective noTeam = getSandboxProject().createRetrospective(
                "No Team");

        Team expectedTeam = getInstance().create().team(
                "Team" + getSandboxName());
        hasTeam.setTeam(expectedTeam);
        hasTeam.save();

        testTeam(hasTeam, noTeam, expectedTeam);
    }

    @Test
    public void testNoTeam() {
        Retrospective hasTeam = getSandboxProject().createRetrospective(
                "Has Team");
        Retrospective noTeam = getSandboxProject().createRetrospective(
                "No Team");

        Team expectedTeam = getInstance().create().team(
                "Team" + getSandboxName());
        hasTeam.setTeam(expectedTeam);
        hasTeam.save();

        testTeam(noTeam, hasTeam, null);
    }

    private void testTeam(Retrospective expected, Retrospective not,
            Team expectedTeam) {
        RetrospectiveFilter filter = new RetrospectiveFilter();
        filter.project.add(getSandboxProject());
        filter.team.add(expectedTeam);

        resetInstance();
        expectedTeam = (expectedTeam != null) ? getInstance().get()
                .teamByID(expectedTeam.getID()) : null;
        expected = getInstance().get()
                .retrospectiveByID(expected.getID());
        not = getInstance().get().retrospectiveByID(not.getID());

        Collection<Retrospective> results = getSandboxProject()
                .getRetrospectives(filter);

        Assert.assertTrue(
                "Expected to find Retrospective that matched filter.",
                findRelated(expected, results));
        Assert.assertFalse(
                        "Expected to NOT find Retrospective that doesn't match filter.",
                        findRelated(not, results));
        for (Retrospective result : results) {
            Assert.assertEquals(expectedTeam, result.getTeam());
        }
    }

}
