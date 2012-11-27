package com.versionone.om.tests;

import com.versionone.DB.DateTime;
import com.versionone.om.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Effort tester.
 * <p>Not exist in .NET
 */
public class EffortTester extends BaseSDKTester {

    @Override
    protected Project createSandboxProject(Project rootProject) {
    	Map<String, Object> attributes = new HashMap<String, Object>();
    	attributes.put("Scheme", getDefaultSchemeOid());
        return getInstance().create().project(getSandboxName(), rootProject, DateTime.now(), getSandboxSchedule(), attributes);
    }

    @Test
    public void testCreateAndDeleteEffort() {
        final double effortValue = 23.1;
        final String defectName = "Defect effort name";
        final DateTime date = DateTime.now();

        final Member member = getInstance().get().memberByID("Member:1000");
        final Team team = getSandboxTeam();
        final Project project = getSandboxProject();
        final Defect defect = project.createDefect(defectName);
        final Iteration iteration = project.createIteration();
        final Effort effort = defect.createEffort(effortValue);
        effort.setDate(date);
        effort.setIteration(iteration);
        effort.setMember(member);
        effort.setTeam(team);
        effort.save();
        resetInstance();

        // verify
        final Effort actuialEffort = new Effort(effort.getID(), getInstance());
        Assert.assertEquals(member, actuialEffort.getMember());
        Assert.assertEquals(iteration, actuialEffort.getIteration());
        Assert.assertEquals(effortValue, actuialEffort.getValue(), ESTIMATES_PRECISION);
        Assert.assertEquals(team, actuialEffort.getTeam());
        Assert.assertEquals(date.getDate(), actuialEffort.getDate());

        // remove all created items
        team.delete();
        iteration.delete();
        defect.delete();
        project.delete();
    }

//    @Test
//    public void testCreateEffortWithAttributes() {
//        final String name = "EffortName";
//        final double effortValue = 23.1;
//        final DateTime date = DateTime.now();
//
//        final Member member = getInstance().get().memberByID("Member:1000");
//        final Team team = getSandboxTeam();
//        final Project project = getSandboxProject();
//        final Defect defect = project.createDefect("DefectForEffort");
//        final Iteration iteration = project.createIteration();
//
//        final String description = "Test for Goal creation with required attributes";
//        Map<String, Object> attributes = new HashMap<String, Object>();
//        attributes.put("Iteration", iteration);
//        attributes.put("Date", date);
//        attributes.put("Member", member);
//        attributes.put("Team", team);
//
//        getInstance().setValidationEnabled(true);
//
//        Effort effort = defect.createEffort(effortValue, member, date, attributes);
//
//        resetInstance();
//
//        effort = getInstance().get().effortByID(effort.getID());
//
//        Assert.assertEquals(member, effort.getMember());
//        Assert.assertEquals(iteration, effort.getIteration());
//        Assert.assertEquals(effortValue, effort.getValue(), ESTIMATES_PRECISION);
//        Assert.assertEquals(team, effort.getTeam());
//        Assert.assertEquals(date.getDate(), effort.getDate());
//
//        getInstance().setValidationEnabled(false);
//    }
    
    @Test
    public void testGetById()
    {
        Effort effort = getInstance().get().effortByID(AssetID.fromToken("Actual:1448"));
        Assert.assertNotNull(effort);
        Assert.assertEquals(4.0, effort.getValue(), 0);
        Assert.assertEquals("Boris Tester", effort.getMember().getName());    	
    }    
}
