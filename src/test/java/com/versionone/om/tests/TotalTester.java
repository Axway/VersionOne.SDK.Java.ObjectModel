/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.HashMap;
import java.util.Map;

import com.versionone.om.BaseAsset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.versionone.Duration;
import com.versionone.DB.DateTime;
import com.versionone.om.Defect;
import com.versionone.om.Epic;
import com.versionone.om.Iteration;
import com.versionone.om.Project;
import com.versionone.om.Schedule;
import com.versionone.om.Story;
import com.versionone.om.Task;
import com.versionone.om.filters.DefectFilter;
import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.filters.SecondaryWorkitemFilter;
import com.versionone.om.filters.StoryFilter;
import com.versionone.om.filters.TaskFilter;
import com.versionone.om.filters.TestFilter;
import com.versionone.om.filters.WorkitemFilter;

public final class TotalTester extends BaseSDKTester {

    private void assertEquals(Double expected, Double actual) {
        if (expected == null)
            Assert.assertNull(actual);
        else
            Assert.assertEquals(expected, actual, ESTIMATES_PRECISION);
    }

    private void assertEquals(Double expected, Double actual, String message) {
        if (expected == null)
            Assert.assertNull(message, actual);
        else
            Assert.assertEquals(message, expected, actual, ESTIMATES_PRECISION);
    }

    private Project getChildrenProject() {
        return getEntityFactory().createSubProject("Son of " + getSandboxName(), DateTime.now(), null, getSandboxProject());
    }

    @Override
    protected Project createSandboxProject(final Project rootProject) {
    	final Map<String, Object> attributes = new HashMap<String, Object>();
    	attributes.put("Scheme", getDefaultSchemeOid());
    	final Schedule schedule = getInstance().create().schedule("Sandbox Schedule", new Duration(7, Duration.Unit.Days), new Duration(0, Duration.Unit.Days));
        
        Project project = getEntityFactory().create(new EntityFactory.IEntityCreator<Project>() {
            public Project create() {
                return getInstance().create().project( getSandboxName(), 
                        rootProject, DateTime.now(), schedule, attributes);
            }
        });
        return project;
    }

    @Before
    public void Setup() {
        newSandboxProject();
        newSandboxIteration();
        newSandboxTeam();
        newSandboxMember();
    }

    @Test
    public void testProjectTotalEstimateSliceByIteration() {
        Story story1 = createStory("Story 1", getSandboxProject());
        Story story2 = createStory("Story 2", getSandboxProject());
        Iteration iteration = getEntityFactory().create(new EntityFactory.IEntityCreator<Iteration>() {
            public Iteration create() {
                return getSandboxProject().createIteration();
            }
        });
        iteration.setName("Test Iteration");
        iteration.save();

        story1.setEstimate(1.);
        story2.setEstimate(2.);

        Story inIteration1 = createStory("In 1", getSandboxProject(), iteration);
        Story inIteration2 = createStory("In 2", getSandboxProject(), iteration);

        Assert.assertEquals(getSandboxProject(), inIteration1.getProject());
        Assert.assertEquals(getSandboxProject(), inIteration2.getProject());

        inIteration1.setEstimate(10.);
        inIteration2.setEstimate(5.);

        story1.save();
        story2.save();
        inIteration1.save();
        inIteration2.save();

        PrimaryWorkitemFilter filter = new PrimaryWorkitemFilter();
        filter.iteration.add(iteration);

        assertEquals(15., getSandboxProject().getTotalEstimate(filter));
    }
    
    private Story createStory(String name, Project rootProject) {
        return getEntityFactory().createStory(name, rootProject);
    }

    ///////////////// Single Level Project Totals //////////////////////////////

    private void assertProjectTotalDone(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalDone(filter));
    }

    @Test
    public void testProjectTotalDone() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        storyA.createTask("Task 1").createEffort(5.);
        defectB.createEffort(10.);

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.createEffort(13.37);
        rogue.delete();

        assertProjectTotalDone(15., null);
        assertProjectTotalDone(15., new WorkitemFilter());
        assertProjectTotalDone(10., new PrimaryWorkitemFilter());
        assertProjectTotalDone(null, new StoryFilter());
        assertProjectTotalDone(10., new DefectFilter());
        assertProjectTotalDone(5., new SecondaryWorkitemFilter());
        assertProjectTotalDone(5., new TaskFilter());
        assertProjectTotalDone(null, new TestFilter());
    }
    
    private Defect createDefect(String name, Project project) {
        return getEntityFactory().createDefect(name, project);
    }

    private void assertProjectTotalToDo(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalToDo(filter));
    }

    @Test
    public void testProjectTotalToDo() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        Task task1 = storyA.createTask("Task 1");
        task1.setToDo(5.);
        defectB.setToDo(10.);

        task1.save();
        defectB.save();

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.setToDo(13.37);
        rogue.save();
        rogue.delete();

        assertProjectTotalToDo(15., null);
        assertProjectTotalToDo(15., new WorkitemFilter());
        assertProjectTotalToDo(10., new PrimaryWorkitemFilter());
        assertProjectTotalToDo(null, new StoryFilter());
        assertProjectTotalToDo(10., new DefectFilter());
        assertProjectTotalToDo(5., new SecondaryWorkitemFilter());
        assertProjectTotalToDo(5., new TaskFilter());
        assertProjectTotalToDo(null, new TestFilter());
    }

    private void assertProjectTotalDetailEstimate(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalDetailEstimate(filter));
    }

    @Test
    public void testProjectTotalDetailEstimate() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        Task task1 = storyA.createTask("Task 1");
        task1.setDetailEstimate(5.);
        defectB.setDetailEstimate(10.);

        task1.save();
        defectB.save();

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.setDetailEstimate(13.37);
        rogue.save();
        rogue.delete();

        assertProjectTotalDetailEstimate(15., null);
        assertProjectTotalDetailEstimate(15., new WorkitemFilter());
        assertProjectTotalDetailEstimate(10., new PrimaryWorkitemFilter());
        assertProjectTotalDetailEstimate(null, new StoryFilter());
        assertProjectTotalDetailEstimate(10., new DefectFilter());
        assertProjectTotalDetailEstimate(5., new SecondaryWorkitemFilter());
        assertProjectTotalDetailEstimate(5., new TaskFilter());
        assertProjectTotalDetailEstimate(null, new TestFilter());
    }

    private void assertProjectTotalEstimate(Double expected, PrimaryWorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalEstimate(filter));
    }

    @Test
    public void testProjectTotalEstimate() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        storyA.setEstimate(5.);
        defectB.setEstimate(10.);

        storyA.save();
        defectB.save();

        assertProjectTotalEstimate(15., null);
        assertProjectTotalEstimate(15., new PrimaryWorkitemFilter());
        assertProjectTotalEstimate(5., new StoryFilter());
        assertProjectTotalEstimate(10., new DefectFilter());
    }

    //////////////////////// MultiLevel Project Totals /////////////////////////

    private void assertProjectTotalDoneAndDown(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalDone(filter, true));
    }

    @Test
    public void testProjectTotalDoneAndDown() {
        Project childProject = getChildrenProject();
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        Story storyC = createStory("Story C", childProject);
        Defect defectD = createDefect("Defect D", childProject);

        storyA.createTask("Task 1").createEffort(5.);
        defectB.createEffort(10.);
        storyC.createTest("Test 1").createEffort(2.);
        defectD.createEffort(1.);

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.createEffort(13.37);
        rogue.delete();

        assertProjectTotalDoneAndDown(18., null);
        assertProjectTotalDoneAndDown(18., new WorkitemFilter());
        assertProjectTotalDoneAndDown(11., new PrimaryWorkitemFilter());
        assertProjectTotalDoneAndDown(null,new StoryFilter());
        assertProjectTotalDoneAndDown(11., new DefectFilter());
        assertProjectTotalDoneAndDown(7., new SecondaryWorkitemFilter());
        assertProjectTotalDoneAndDown(5., new TaskFilter());
        assertProjectTotalDoneAndDown(2., new TestFilter());
    }

    private void assertProjectTotalToDoAndDown(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalToDo(filter, true));
    }

    @Test
    public void testProjectTotalToDoAndDown() {
        Project childProject = getChildrenProject();
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        Story storyC = createStory("Story C", childProject);
        Defect defectD = createDefect("Defect D", childProject);

        Task task1 = storyA.createTask("Task 1");
        task1.setToDo(5.);
        defectB.setToDo(10.);

        com.versionone.om.Test test1 = storyC.createTest("Test 1");
        test1.setToDo(2.);
        defectD.setToDo(3.);

        task1.save();
        defectB.save();
        test1.save();
        defectD.save();

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.setToDo(13.37);
        rogue.save();
        rogue.delete();

        assertProjectTotalToDoAndDown(20., null);
        assertProjectTotalToDoAndDown(20., new WorkitemFilter());
        assertProjectTotalToDoAndDown(13., new PrimaryWorkitemFilter());
        assertProjectTotalToDoAndDown(null, new StoryFilter());
        assertProjectTotalToDoAndDown(13., new DefectFilter());
        assertProjectTotalToDoAndDown(7., new SecondaryWorkitemFilter());
        assertProjectTotalToDoAndDown(5., new TaskFilter());
        assertProjectTotalToDoAndDown(2., new TestFilter());
    }

    private void assertProjectTotalDetailEstimateAndDown(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalDetailEstimate(filter, true));
    }

    @Test
    public void testProjectTotalDetailEstimateAndDown() {
        Project childProject = getChildrenProject();
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        Story storyC = createStory("Story C", childProject);
        Defect defectD = createDefect("Defect D", childProject);

        Task task1 = storyA.createTask("Task 1");
        task1.setDetailEstimate(5.);
        defectB.setDetailEstimate(10.);

        com.versionone.om.Test test1 = storyC.createTest("Test 1");
        test1.setDetailEstimate(2.);
        defectD.setDetailEstimate(3.);

        task1.save();
        defectB.save();
        test1.save();
        defectD.save();

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.setDetailEstimate(13.37);
        rogue.save();
        rogue.delete();

        assertProjectTotalDetailEstimateAndDown(20., null);
        assertProjectTotalDetailEstimateAndDown(20., new WorkitemFilter());
        assertProjectTotalDetailEstimateAndDown(13., new PrimaryWorkitemFilter());
        assertProjectTotalDetailEstimateAndDown(null,new StoryFilter());
        assertProjectTotalDetailEstimateAndDown(13., new DefectFilter());
        assertProjectTotalDetailEstimateAndDown(7., new SecondaryWorkitemFilter());
        assertProjectTotalDetailEstimateAndDown(5., new TaskFilter());
        assertProjectTotalDetailEstimateAndDown(2., new TestFilter());
    }

    private void assertProjectTotalEstimateAndDown(Double expected, PrimaryWorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalEstimate(filter, true));
    }

    @Test
    public void testProjectTotalEstimateAndDown() {
        Project childProject = getChildrenProject();
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        Story storyC = createStory("Story C", childProject);
        Defect defectD = createDefect("Defect D", childProject);

        storyA.setEstimate(5.);
        defectB.setEstimate(10.);

        storyC.setEstimate(2.);
        defectD.setEstimate(3.);

        storyA.save();
        defectB.save();
        storyC.save();
        defectD.save();

        assertProjectTotalEstimateAndDown(20., null);
        assertProjectTotalEstimateAndDown(20., new PrimaryWorkitemFilter());
        assertProjectTotalEstimateAndDown(7., new StoryFilter());
        assertProjectTotalEstimateAndDown(13., new DefectFilter());
    }

    private void assertProjectTotalEstimateWithClosedChild(Double expected, PrimaryWorkitemFilter filter) {
        assertEquals(expected, getSandboxProject().getTotalEstimate(filter, true),
                "Expect to exclude Open workitems in closed child projects");
    }

    @Test
    public void testProjectTotalEstimateWithClosedChild() {
        Project childProject = getChildrenProject();
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        Story storyC = createStory("Story C", childProject);
        Defect defectD = createDefect("Defect D", childProject);

        storyA.setEstimate(5.);
        defectB.setEstimate(10.);

        storyC.setEstimate(2.);
        defectD.setEstimate(3.);

        storyA.save();
        defectB.save();
        storyC.save();
        defectD.save();

        // Close child project:
        childProject.close();
        resetInstance();

        // Expect to exclude Open workitems in closed child projects
        assertProjectTotalEstimateWithClosedChild(15., null);
        assertProjectTotalEstimateWithClosedChild(15., new PrimaryWorkitemFilter());
        assertProjectTotalEstimateWithClosedChild(5., new StoryFilter());
        assertProjectTotalEstimateWithClosedChild(10., new DefectFilter());
    }

    ////////////////////////// Iteration Totals ////////////////////////////////

    private void assertIterationTotalDone(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxIteration().getTotalDone(filter));
    }

    @Test
    public void testIterationTotalDone() {
        Story storyA = createStory("Story A", getSandboxProject(), getSandboxIteration());
        Defect defectB = createDefect("Defect B", getSandboxProject(), getSandboxIteration());

        storyA.createTask("Task 1").createEffort(5.);
        defectB.createEffort(10.);

        Defect rogue = createDefect("Rogue", getSandboxProject(), getSandboxIteration());
        rogue.createEffort(13.37);
        rogue.delete();

        assertIterationTotalDone(15., null);
        assertIterationTotalDone(15., new WorkitemFilter());
        assertIterationTotalDone(10., new PrimaryWorkitemFilter());
        assertIterationTotalDone(null, new StoryFilter());
        assertIterationTotalDone(10., new DefectFilter());
        assertIterationTotalDone(5., new SecondaryWorkitemFilter());
        assertIterationTotalDone(5., new TaskFilter());
        assertIterationTotalDone(null, new TestFilter());
    }

    private void assertIterationTotalToDo(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxIteration().getTotalToDo(filter));
    }

    @Test
    public void testIterationTotalToDo() {
        Story storyA = createStory("Story A", getSandboxProject(), getSandboxIteration());
        Defect defectB = createDefect("Defect B", getSandboxProject(), getSandboxIteration());

        Task task1 = storyA.createTask("Task 1");
        task1.setToDo(5.);
        defectB.setToDo(10.);

        task1.save();
        defectB.save();

        Defect rogue = createDefect("Rogue", getSandboxProject(), getSandboxIteration());
        rogue.setToDo(13.37);
        rogue.save();
        rogue.delete();

        assertIterationTotalToDo(15., null);
        assertIterationTotalToDo(15., new WorkitemFilter());
        assertIterationTotalToDo(10., new PrimaryWorkitemFilter());
        assertIterationTotalToDo(null, new StoryFilter());
        assertIterationTotalToDo(10., new DefectFilter());
        assertIterationTotalToDo(5., new SecondaryWorkitemFilter());
        assertIterationTotalToDo(5., new TaskFilter());
        assertIterationTotalToDo(null, new TestFilter());
    }

    private void assertIterationTotalDetailEstimate(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxIteration().getTotalDetailEstimate(filter));
    }

    @Test
    public void testIterationTotalDetailEstimate() {
        Story storyA = createStory("Story A", getSandboxProject(), getSandboxIteration());
        Defect defectB = createDefect("Defect B", getSandboxProject(), getSandboxIteration());

        Task task1 = storyA.createTask("Task 1");
        task1.setDetailEstimate(5.);
        defectB.setDetailEstimate(10.);

        task1.save();
        defectB.save();

        Defect rogue = createDefect("Rogue", getSandboxProject(), getSandboxIteration());
        rogue.setDetailEstimate(13.37);
        rogue.save();
        rogue.delete();

        assertIterationTotalDetailEstimate(15., null);
        assertIterationTotalDetailEstimate(15., new WorkitemFilter());
        assertIterationTotalDetailEstimate(10., new PrimaryWorkitemFilter());
        assertIterationTotalDetailEstimate(null, new StoryFilter());
        assertIterationTotalDetailEstimate(10., new DefectFilter());
        assertIterationTotalDetailEstimate(5., new SecondaryWorkitemFilter());
        assertIterationTotalDetailEstimate(5., new TaskFilter());
        assertIterationTotalDetailEstimate(null, new TestFilter());
    }

    private void assertIterationTotalEstimate(Double expected, PrimaryWorkitemFilter filter) {
        assertEquals(expected, getSandboxIteration().getTotalEstimate(filter));
    }

    @Test
    public void testIterationTotalEstimate() {
        Story storyA = createStory("Story A", getSandboxProject(), getSandboxIteration());
        Defect defectB = createDefect("Defect B", getSandboxProject(), getSandboxIteration());

        storyA.setEstimate(5.);
        defectB.setEstimate(10.);

        storyA.save();
        defectB.save();

        Story rogue = createStory("Rogue", getSandboxProject(), getSandboxIteration());
        rogue.setEstimate(13.37);
        rogue.save();
        rogue.delete();

        assertIterationTotalEstimate(15., null);
        assertIterationTotalEstimate(15., new PrimaryWorkitemFilter());
        assertIterationTotalEstimate(5., new StoryFilter());
        assertIterationTotalEstimate(10., new DefectFilter());
    }

    //////////////////////////// Team Totals /////////////////////////////////

    private void assertTeamTotalDone(Double expected, WorkitemFilter filter) {
        assertEquals(expected, getSandboxTeam().getTotalDone(filter));
    }

    @Test
    public void testTeamTotalDone() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());
        Task task1 = storyA.createTask("Task 1");

        storyA.setTeam(getSandboxTeam());
        defectB.setTeam(getSandboxTeam());

        storyA.save();
        defectB.save();

        task1.createEffort(5.);
        defectB.createEffort(10.);

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.setTeam(getSandboxTeam());
        rogue.save();
        rogue.createEffort(13.37);
        rogue.delete();

        assertTeamTotalDone(15., null);
        assertTeamTotalDone(15., new WorkitemFilter());
        assertTeamTotalDone(10., new PrimaryWorkitemFilter());
        assertTeamTotalDone(null, new StoryFilter());
        assertTeamTotalDone(10., new DefectFilter());
        assertTeamTotalDone(5., new SecondaryWorkitemFilter());
        assertTeamTotalDone(5., new TaskFilter());
        assertTeamTotalDone(null, new TestFilter());
    }

    @Test
    public void TeamTotalToDo() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());
        Task task1 = storyA.createTask("Task 1");

        storyA.setTeam(getSandboxTeam());
        defectB.setTeam(getSandboxTeam());

        task1.setToDo(5.);
        defectB.setToDo(10.);

        task1.save();
        storyA.save();
        defectB.save();

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.setTeam(getSandboxTeam());
        rogue.setToDo(13.37);
        rogue.save();
        rogue.delete();

        assertEquals(15., getSandboxTeam().getTotalToDo(null));
        assertEquals(15., getSandboxTeam().getTotalToDo(new WorkitemFilter()));
        assertEquals(10., getSandboxTeam().getTotalToDo(new PrimaryWorkitemFilter()));
        assertEquals(null,getSandboxTeam().getTotalToDo(new StoryFilter()));
        assertEquals(10., getSandboxTeam().getTotalToDo(new DefectFilter()));
        assertEquals(5., getSandboxTeam().getTotalToDo(new SecondaryWorkitemFilter()));
        assertEquals(5., getSandboxTeam().getTotalToDo(new TaskFilter()));
        assertEquals(null,getSandboxTeam().getTotalToDo(new TestFilter()));
    }

    @Test
    public void TeamTotalDetailEstimate() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());
        Task task1 = storyA.createTask("Task 1");

        storyA.setTeam(getSandboxTeam());
        defectB.setTeam(getSandboxTeam());

        task1.setDetailEstimate(5.);
        defectB.setDetailEstimate(10.);

        task1.save();
        storyA.save();
        defectB.save();

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.setTeam(getSandboxTeam());
        rogue.setDetailEstimate(13.37);
        rogue.save();
        rogue.delete();

        assertEquals(15., getSandboxTeam().getTotalDetailEstimate(null));
        assertEquals(15., getSandboxTeam().getTotalDetailEstimate(new WorkitemFilter()));
        assertEquals(10., getSandboxTeam().getTotalDetailEstimate(new PrimaryWorkitemFilter()));
        assertEquals(null,getSandboxTeam().getTotalDetailEstimate(new StoryFilter()));
        assertEquals(10., getSandboxTeam().getTotalDetailEstimate(new DefectFilter()));
        assertEquals(5., getSandboxTeam().getTotalDetailEstimate(new SecondaryWorkitemFilter()));
        assertEquals(5., getSandboxTeam().getTotalDetailEstimate(new TaskFilter()));
        assertEquals(null,getSandboxTeam().getTotalDetailEstimate(new TestFilter()));
    }

    @Test
    public void TeamTotalEstimate() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        storyA.setTeam(getSandboxTeam());
        defectB.setTeam(getSandboxTeam());

        storyA.setEstimate(5.);
        defectB.setEstimate(10.);

        storyA.save();
        defectB.save();

        Story rogue = getSandboxProject().createStory("Rogue");
        rogue.setTeam(getSandboxTeam());
        rogue.setEstimate(13.37);
        rogue.save();
        rogue.delete();

        assertEquals(15., getSandboxTeam().getTotalEstimate(null));
        assertEquals(15., getSandboxTeam().getTotalEstimate(new PrimaryWorkitemFilter()));
        assertEquals(5., getSandboxTeam().getTotalEstimate(new StoryFilter()));
        assertEquals(10., getSandboxTeam().getTotalEstimate(new DefectFilter()));
    }

    //////////////////////////// Member Totals /////////////////////////////////

    @Test
    public void MemberTotalDone() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());
        Task task1 = storyA.createTask("Task 1");

        storyA.getOwners().add(getSandboxMember());
        defectB.getOwners().add(getSandboxMember());
        task1.getOwners().add(getSandboxMember());

        storyA.save();
        defectB.save();

        task1.createEffort(5.);
        defectB.createEffort(10.);

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.getOwners().add(getSandboxMember());
        rogue.save();
        rogue.createEffort(13.37);
        rogue.delete();

        assertEquals(15., getSandboxMember().getTotalDone(null));
        assertEquals(15., getSandboxMember().getTotalDone(new WorkitemFilter()));
        assertEquals(10., getSandboxMember().getTotalDone(new PrimaryWorkitemFilter()));
        assertEquals(null,getSandboxMember().getTotalDone(new StoryFilter()));
        assertEquals(10., getSandboxMember().getTotalDone(new DefectFilter()));
        assertEquals(5., getSandboxMember().getTotalDone(new SecondaryWorkitemFilter()));
        assertEquals(5., getSandboxMember().getTotalDone(new TaskFilter()));
        assertEquals(null,getSandboxMember().getTotalDone(new TestFilter()));
    }

    @Test
    public void MemberTotalToDo() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());
        Task task1 = storyA.createTask("Task 1");

        storyA.getOwners().add(getSandboxMember());
        defectB.getOwners().add(getSandboxMember());
        task1.getOwners().add(getSandboxMember());

        task1.setToDo(5.);
        defectB.setToDo(10.);

        task1.save();
        storyA.save();
        defectB.save();

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.getOwners().add(getSandboxMember());
        rogue.setToDo(13.37);
        rogue.save();
        rogue.delete();

        assertEquals(15., getSandboxMember().getTotalToDo(null));
        assertEquals(15., getSandboxMember().getTotalToDo(new WorkitemFilter()));
        assertEquals(10., getSandboxMember().getTotalToDo(new PrimaryWorkitemFilter()));
        assertEquals(null,getSandboxMember().getTotalToDo(new StoryFilter()));
        assertEquals(10., getSandboxMember().getTotalToDo(new DefectFilter()));
        assertEquals(5., getSandboxMember().getTotalToDo(new SecondaryWorkitemFilter()));
        assertEquals(5., getSandboxMember().getTotalToDo(new TaskFilter()));
        assertEquals(null,getSandboxMember().getTotalToDo(new TestFilter()));
    }

    @Test
    public void MemberTotalDetailEstimate() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());
        Task task1 = storyA.createTask("Task 1");

        storyA.getOwners().add(getSandboxMember());
        defectB.getOwners().add(getSandboxMember());
        task1.getOwners().add(getSandboxMember());

        task1.setDetailEstimate(5.);
        defectB.setDetailEstimate(10.);

        task1.save();
        storyA.save();
        defectB.save();

        Defect rogue = getSandboxProject().createDefect("Rogue");
        rogue.getOwners().add(getSandboxMember());
        rogue.setDetailEstimate(13.37);
        rogue.save();
        rogue.delete();

        assertEquals(15., getSandboxMember().getTotalDetailEstimate(null));
        assertEquals(15., getSandboxMember().getTotalDetailEstimate(new WorkitemFilter()));
        assertEquals(10., getSandboxMember().getTotalDetailEstimate(new PrimaryWorkitemFilter()));
        assertEquals(null,getSandboxMember().getTotalDetailEstimate(new StoryFilter()));
        assertEquals(10., getSandboxMember().getTotalDetailEstimate(new DefectFilter()));
        assertEquals(5., getSandboxMember().getTotalDetailEstimate(new SecondaryWorkitemFilter()));
        assertEquals(5., getSandboxMember().getTotalDetailEstimate(new TaskFilter()));
        assertEquals(null,getSandboxMember().getTotalDetailEstimate(new TestFilter()));
    }

    @Test
    public void MemberTotalEstimate() {
        Story storyA = createStory("Story A", getSandboxProject());
        Defect defectB = createDefect("Defect B", getSandboxProject());

        storyA.getOwners().add(getSandboxMember());
        defectB.getOwners().add(getSandboxMember());

        storyA.setEstimate(5.);
        defectB.setEstimate(10.);

        storyA.save();
        defectB.save();

        Epic rogue = getEntityFactory().createEpic("Rogue", getSandboxProject());
        rogue.getOwners().add(getSandboxMember());
        rogue.save();

        assertEquals(15., getSandboxMember().getTotalEstimate(null));
        assertEquals(15., getSandboxMember().getTotalEstimate(new PrimaryWorkitemFilter()));
        assertEquals(5., getSandboxMember().getTotalEstimate(new StoryFilter()));
        assertEquals(10., getSandboxMember().getTotalEstimate(new DefectFilter()));
    }

    //////////////////////// PrimaryWorkitem ///////////////////////////////////

    @Test
    public void WorkitemTotalDetailEstimate() {
        Story story = createStory("Story 1", getSandboxProject());
        Task task1 = story.createTask("Task 1");
        com.versionone.om.Test test1 = story.createTest("Test 1");
        Defect defect = createDefect("Defect 1", getSandboxProject());

        task1.setDetailEstimate(10.);
        test1.setDetailEstimate(5.);
        defect.setDetailEstimate(3.);
        task1.save();
        test1.save();
        defect.save();

        Task rogue = story.createTask("Rogue");
        rogue.setDetailEstimate(13.37);
        rogue.save();
        rogue.delete();

        assertEquals(15., story.getTotalDetailEstimate(null));
        assertEquals(15., story.getTotalDetailEstimate(new WorkitemFilter()));
        assertEquals(10., story.getTotalDetailEstimate(new TaskFilter()));
        assertEquals(5., story.getTotalDetailEstimate(new TestFilter()));
        assertEquals(3., defect.getTotalDetailEstimate(new PrimaryWorkitemFilter()));
        assertEquals(3., defect.getTotalDetailEstimate(new DefectFilter()));
    }

    @Test
    public void WorkitemTotalToDo() {
        Story story = createStory("Story 1", getSandboxProject());
        Task task1 = story.createTask("Task 1");
        com.versionone.om.Test test1 = story.createTest("Test 1");
        Defect defect = createDefect("Defect 1", getSandboxProject());

        task1.setToDo(10.);
        test1.setToDo(5.);
        defect.setToDo(3.);
        task1.save();
        test1.save();
        defect.save();

        Task rogue = story.createTask("Rogue");
        rogue.setToDo(13.37);
        rogue.save();
        rogue.delete();

        assertEquals(15., story.getTotalToDo(null));
        assertEquals(15., story.getTotalToDo(new WorkitemFilter()));
        assertEquals(10., story.getTotalToDo(new TaskFilter()));
        assertEquals(5., story.getTotalToDo(new TestFilter()));
        assertEquals(3., defect.getTotalToDo(new PrimaryWorkitemFilter()));
        assertEquals(3., defect.getTotalToDo(new DefectFilter()));
    }

    @Test
    public void WorkitemTotalDone() {
        Story story = createStory("Story 1", getSandboxProject());
        Task task1 = story.createTask("Task 1");
        com.versionone.om.Test test1 = story.createTest("Test 1");
        Defect defect = createDefect("Defect 1", getSandboxProject());

        task1.createEffort(10.);
        test1.createEffort(5.);
        defect.createEffort(3);

        Task rogue = story.createTask("Rogue");
        rogue.createEffort(13.37);
        rogue.delete();

        assertEquals(15., story.getTotalDone(null));
        assertEquals(15., story.getTotalDone(new WorkitemFilter()));
        assertEquals(10., story.getTotalDone(new TaskFilter()));
        assertEquals(5., story.getTotalDone(new TestFilter()));
        assertEquals(3., defect.getTotalDone(new PrimaryWorkitemFilter()));
        assertEquals(3., defect.getTotalDone(new DefectFilter()));
    }
}
