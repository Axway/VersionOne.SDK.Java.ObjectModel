package com.versionone.om.tests;


import com.versionone.om.Environment;
import com.versionone.om.Project;
import com.versionone.om.filters.EnvironmentFilter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class EnvironmentFilterTester extends BaseSDKTester {
    private String EnvironmentName = "Environment for test";


    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Test
    public void getEnvironmentWithNullFilterTest() {
        Collection<Environment> environments = getInstance().get().environments(null);
        Environment environment = createEnvironment(EnvironmentName);
        Collection<Environment> updatedEnvironments = getInstance().get().environments(null);

        Assert.assertEquals(1, updatedEnvironments.size() - environments.size());
        ListAssert.contains(environment, updatedEnvironments);
    }

    @Test
    public void getEnvironmentByName() {
        Environment environment = createEnvironment(EnvironmentName);

        EnvironmentFilter filter = new EnvironmentFilter();
        filter.name.add(EnvironmentName);
        Collection<Environment> environments = getInstance().get().environments(filter);
        ListAssert.contains(environment, environments);

        filter = new EnvironmentFilter();
        filter.name.add("WrongName");
        environments = getInstance().get().environments(filter);
        Assert.assertEquals(0, environments.size());
    }

    @Test
    public void getEnvironmentWithSpecificProjectTest() {
        Project project2 = getEntityFactory().createProject("other", getSandboxProject(), null);
        Environment environment = createEnvironment(EnvironmentName);

        EnvironmentFilter filter = new EnvironmentFilter();
        filter.project.add(getSandboxProject());
        Collection<Environment> environments = getInstance().get().environments(filter);
        ListAssert.contains(environment, environments);

        filter = new EnvironmentFilter();
        filter.project.add(project2);
        environments = getInstance().get().environments(filter);
        Assert.assertEquals(0, environments.size());
    }

    @Test
    public void getEnvironmentByID() {
        Environment environment = createEnvironment(EnvironmentName);
        String displayId = environment.getDisplayID();

        EnvironmentFilter filter = new EnvironmentFilter();
        filter.displayID.add(displayId);
        Collection<Environment> environments = getInstance().get().environments(filter);
        Assert.assertEquals(1, environments.size());
        ListAssert.contains(environment, environments);

        filter = new EnvironmentFilter();
        filter.displayID.add("WrongNumber");
        environments = getInstance().get().environments(filter);
        Assert.assertEquals(0, environments.size());
    }

    private Environment createEnvironment(String name) {
        return createEnvironment(name, new HashMap<String, Object>());
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }
}