package com.versionone.om.tests;

import com.versionone.DB.DateTime;
import com.versionone.om.Iteration;
import com.versionone.om.Project;
import com.versionone.om.filters.IterationFilter;
import com.versionone.om.filters.IterationFilter.IterationState;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class IterationFilterTester extends BaseSDKTester {
    @Override
    protected Project createSandboxProject(Project rootProject) {
    	Map<String, Object> attributes = new HashMap<String, Object>();
    	attributes.put("Scheme", getDefaultSchemeOid());
        return getInstance().create().project(
                "IterationFilterTester",
                getInstance().get().projectByID("Scope:0"),
                DateTime.now(),
                getSandboxSchedule(),
                attributes);
    }

    @Test
    public void futureState() {
        Iteration iteration = getSandboxProject().createIteration();
        iteration.setName("Test Iteration");
        iteration.save();

        IterationFilter filter = new IterationFilter();
        filter.schedule.add(getSandboxSchedule());
        filter.state.add(IterationState.Future);
        Assert.assertEquals(1, getInstance().get().iterations(filter).size());
    }

    @Test
    public void activeState() {
        Iteration iteration = getSandboxProject().createIteration();
        iteration.setName("Test Iteration");
        iteration.save();
        iteration.activate();

        IterationFilter filter = new IterationFilter();
        filter.schedule.add(getSandboxSchedule());
        filter.state.add(IterationState.Active);
        Assert.assertEquals(1, getInstance().get().iterations(filter).size());
    }
}
