/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ThemeTester extends BaseSDKTester {
    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Test
    public void testCreateAndRetrieveTheme() {
        final String name = "New Name";
        AssetID id = createTheme(name, null).getID();
        resetInstance();

        Theme theme = getInstance().get().themeByID(id);
        Assert.assertEquals(theme.getName(), name);
    }

    /**
     * <p>Not exist in .NET
     */
    @Test
    public void testThemeAttributes() {
        final String name = "New Name";
        final String build = "0.0.1";
        final double estimate = 12.;
        final String sourceValue = "Development";

        final Project project = getSandboxProject();
        final Member customer = getSandboxMember();
        Theme theme = getInstance().create().theme(name, project);
        theme.setCustomer(customer);
        theme.setBuild(build);
        theme.setEstimate(estimate);
        IListValueProperty source = theme.getSource();
        source.setCurrentValue(sourceValue);

        theme.save();
        final AssetID id = theme.getID();

        resetInstance();

        theme = getInstance().get().themeByID(id);
        Assert.assertEquals(customer, theme.getCustomer());
        Assert.assertEquals(build, theme.getBuild());
        Assert.assertEquals(estimate, theme.getEstimate(), ESTIMATES_PRECISION);

        final Collection<Member> owners = theme.getOwners();

        // created theme doesn't have any owners
        Assert.assertEquals(0, owners.size());

        source = theme.getSource();
        ListAssert.areEqualIgnoringOrder(Arrays.asList(source.getAllValues()),
                "Product Mgt", "Sales", "Customer", "Development");
        Assert.assertEquals(sourceValue, source.getCurrentValue());

        final Collection<Goal> goals = theme.getGoals();
        //created theme doesn't have any goals
        Assert.assertEquals(0, goals.size());

        Assert.assertTrue(theme.canClose());
        Assert.assertFalse(theme.canReactivate());

        Assert.assertTrue(theme.isActive());
        Assert.assertFalse(theme.isClosed());

        // close theme
        theme.close();
        theme.save();

        resetInstance();

        theme = getInstance().get().themeByID(id);

        Assert.assertFalse(theme.isActive());
        Assert.assertTrue(theme.isClosed());

        Assert.assertFalse(theme.canClose());
        Assert.assertTrue(theme.canReactivate());

        // reactivate theme
        theme.reactivate();
        theme.save();

        resetInstance();

        theme = getInstance().get().themeByID(id);

        Assert.assertTrue(theme.canClose());
        Assert.assertFalse(theme.canReactivate());

        Assert.assertTrue(theme.isActive());
        Assert.assertFalse(theme.isClosed());

        Assert.assertTrue(theme.canDelete());

        //delete theme
        theme.delete();

        resetInstance();

        theme = getInstance().get().themeByID(id);
        Assert.assertNull(theme);

        //clear created data
        customer.delete();
        project.delete();
    }

    @Test
    public void testCreateThemeWithAttributes() {
        final String name = "ThemeSuiteName";
        final String build = "0.0.1";

        final String description = "Test for Theme creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);
        attributes.put("LastVersion", build);

        //Theme theme = getInstance().create().theme(name, project, attributes);
        Theme theme = createTheme(name, attributes);

        Assert.assertEquals(name, theme.getName());
        Assert.assertEquals(description, theme.getDescription());
        Assert.assertEquals(build, theme.getBuild());
    }

    /**
     * <p>Not exist in .NET
     */
    @Test
    public void testChildTheme() {
        final String name = "New Name";

        Theme theme = createTheme(name, null);
        Theme subTheme = createSubTheme(theme, "Child Theme");
        resetInstance();

        final Theme childTheme = getInstance().get().themeByID(subTheme.getID());
        theme = getInstance().get().themeByID(theme.getID());

        Assert.assertEquals(childTheme.getParentTheme(), theme);
        final Collection<Theme> children = theme.getChildThemes(null);
        ListAssert.areEqualIgnoringOrder(children, childTheme);
    }
    
    @Test
    public void testDetailEstimationTheme() {
        double detailEstimateStory1 = 12.1;
        double detailEstimateStory2 = 23.3;

        Theme theme = createTheme("FG name", null);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("DetailEstimate", detailEstimateStory1);
        Story story1 = createStoryUnderTheme("story1", theme);
        createTask("task1", story1, attributes);

        attributes = new HashMap<String, Object>();
        attributes.put("DetailEstimate", detailEstimateStory2);
        Story story2 = createStoryUnderTheme("story2", theme);
        createTask("task2", story2, attributes);

        Assert.assertEquals(detailEstimateStory1 + detailEstimateStory2, theme.getTotalDetailEstimate(null), ESTIMATES_PRECISION);
    }

    @Test
    public void testDetailEstimationThemeWithSubTheme() {
        double detailEstimateStory1 = 12.1;
        double detailEstimateStory2 = 23.3;

        Theme theme1 = createTheme("FG name", null);
        Theme theme2 = createSubTheme(theme1, "test sub FG 1");

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("DetailEstimate", detailEstimateStory1);
        Story story1 = createStoryUnderTheme("story1", theme1);
        createTask("task1", story1, attributes);

        attributes = new HashMap<String, Object>();
        attributes.put("DetailEstimate", detailEstimateStory2);
        Story story2 = createStoryUnderTheme("story2", theme2);
        createTask("task2", story2, attributes);

        Assert.assertEquals(detailEstimateStory2, theme2.getTotalDetailEstimate(null), ESTIMATES_PRECISION);
        Assert.assertEquals(detailEstimateStory1 + detailEstimateStory2, theme1.getTotalDetailEstimate(null), ESTIMATES_PRECISION);
    }

    @Test
    public void testTotalToDoTheme() {
        double toDoStory1 = 12.1;
        double toDoStory2 = 23.3;

        Theme theme1 = createTheme("FG name", null);
        Theme theme2 = createSubTheme(theme1, "test sub FG 1");

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("ToDo", toDoStory1);
        Story story1 = createStoryUnderTheme("story1", theme1);
        createTask("task1", story1, attributes);

        attributes = new HashMap<String, Object>();
        attributes.put("ToDo", toDoStory2);
        Story story2 = createStoryUnderTheme("story2", theme2);
        createTask("task2", story2, attributes);

        Assert.assertEquals(toDoStory2, theme2.getTotalToDo(null), ESTIMATES_PRECISION);
        Assert.assertEquals(toDoStory1 + toDoStory2, theme1.getTotalToDo(null), ESTIMATES_PRECISION);
    }

    @Test
    public void testTotalToDoThemeWithSubTheme() {
        double toDoStory1 = 12.1;
        double toDoStory2 = 23.3;

        Theme theme = createTheme("FG name", null);

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("ToDo", toDoStory1);
        Story story1 = createStoryUnderTheme("story1", theme);
        createTask("task1", story1, attributes);

        attributes = new HashMap<String, Object>();
        attributes.put("ToDo", toDoStory2);
        Story story2 = createStoryUnderTheme("story2", theme);
        createTask("task2", story2, attributes);

        Assert.assertEquals(toDoStory1 + toDoStory2, theme.getTotalToDo(null), ESTIMATES_PRECISION);
    }

    private Theme createSubTheme(final Theme theme1, final String themeName) {
        return getEntityFactory().create(new EntityFactory.IEntityCreator<Theme>() {
                public Theme create() {
                    return theme1.createChildTheme(themeName);
                }
            });
    }

    private Theme createTheme(final String themeName, final Map<String, Object> attributes) {
        return getEntityFactory().create(new EntityFactory.IEntityCreator<Theme>() {
                public Theme create() {
                    return getInstance().create().theme(themeName, getSandboxProject(), attributes);
                }
            });
    }

    private Story createStoryUnderTheme(String storyName, Theme theme) {
        Story story = getEntityFactory().createStory(storyName, getSandboxProject());
        story.setTheme(theme);
        story.save();

        return story;
    }
    
    private Task createTask(String taskName, Story story, Map<String, Object> attributes) {
        return getEntityFactory().createTask(taskName, story, attributes);
    }
}
