package com.versionone.om.tests;

import com.versionone.Oid;
import com.versionone.apiclient.OidException;
import com.versionone.om.Environment;
import com.versionone.om.Project;
import com.versionone.om.filters.EnvironmentFilter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class EnvironmentTester extends BaseSDKTester {
    private final String environmentName = "Environment 1";
    private final String environmentNameUpdated = "Environment 1 Upd";


    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Test
    public void createEnvironmentTest() {
        Environment environment = createEnvironment(environmentName, null);

        EnvironmentFilter filter = new EnvironmentFilter();
        filter.name.add(environmentName);

        resetInstance();

        List<Environment> environments = new ArrayList<Environment>(getInstance().get().environments(filter));
        ListAssert.contains(environment, environments);
        Assert.assertEquals(environmentName, environments.get(0).getName());
    }

    @Test
    public void createEnvironmentWithAttributesTest() throws OidException {
        final String newName = "AnotherName";
        Project newProject = getEntityFactory().createProject("new project", getSandboxProject(), null);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Name", newName);
        attributes.put("Scope", Oid.fromToken(newProject.getID().toString(),
                                              getInstance().getApiClient().getMetaModel()));
        Environment environment = createEnvironment(environmentName, attributes);

        EnvironmentFilter filter = new EnvironmentFilter();
        filter.name.add(newName);
        filter.project.add(newProject);

        resetInstance();

        List<Environment> environments = new ArrayList<Environment>(getInstance().get().environments(filter));
        ListAssert.contains(environment, environments);
        Assert.assertEquals(newProject, environments.get(0).getProject());
        Assert.assertEquals(newName, environments.get(0).getName());
    }

    @Test
    public void updateEnvironmentTest() {
        Environment environment = createEnvironment(environmentName, null);
        resetInstance();

        Environment environmentNew = getInstance().get().environmentByID(environment.getID());
        Assert.assertEquals(environmentName, environmentNew.getName());
        Assert.assertEquals(environment, environmentNew);

        environment.setName(environmentNameUpdated);
        environment.save();

        EnvironmentFilter filter = new EnvironmentFilter();
        filter.name.add(environmentNameUpdated);
        filter.project.add(getSandboxProject());

        resetInstance();

        List<Environment> environments = new ArrayList<Environment>(getInstance().get().environments(filter));
        Assert.assertEquals(environmentNameUpdated, environments.get(0).getName());
        ListAssert.contains(environment, environments);
    }

    @Test
    public void getEnvironmentByIdTest() {
        Environment environment = createEnvironment(environmentName, null);

        resetInstance();

        Environment queriedEnvironment = getInstance().get().environmentByID(environment.getID());
        Assert.assertEquals(environmentName, queriedEnvironment.getName());
    }

    @Test
    public void canCloseTest() {
        Environment environment = createEnvironment(environmentName, null);

        Assert.assertTrue(environment.canClose());
        Assert.assertFalse(environment.isClosed());

        environment.close();

        Assert.assertFalse(environment.canClose());
        Assert.assertTrue(environment.isClosed());
    }

    @Test
    public void canReactivateTest() {
        Environment environment = createEnvironment(environmentName, null);

        Assert.assertFalse(environment.canReactivate());
        Assert.assertTrue(environment.isActive());

        environment.close();

        Assert.assertTrue(environment.canReactivate());
        Assert.assertFalse(environment.isActive());

        environment.reactivate();

        Assert.assertFalse(environment.canReactivate());
        Assert.assertTrue(environment.isActive());
        Assert.assertFalse(environment.isClosed());
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }
}
