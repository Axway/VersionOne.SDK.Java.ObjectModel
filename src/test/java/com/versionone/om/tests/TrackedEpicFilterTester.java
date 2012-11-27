package com.versionone.om.tests;

import com.versionone.om.Epic;
import com.versionone.om.Project;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

// TODO change to explicit filter usage or move these tests
public class TrackedEpicFilterTester extends BaseSDKTester {
    @Test
    public void getTrackedEpicsBySingleProject() {
        final String firstTopLevelEpicName = "first top level epic";
        final String secondTopLevelEpicName = "second top level epic";
        final String childEpicName = "child epic";

        final Project project = getSandboxProject();
        Epic topLevelEpic = getEntityFactory().createEpic(firstTopLevelEpicName, project);
        Epic childEpic = getEntityFactory().createChildEpic(topLevelEpic);
        childEpic.setName(childEpicName);
        childEpic.save();
        getEntityFactory().createEpic(secondTopLevelEpicName, project);

        resetInstance();

        Collection<Project> projects = new ArrayList<Project>();
        projects.add(project);
        Collection<Epic> epics = getInstance().get().trackedEpics(projects);
        Collection<String> epicNames = new ArrayList<String>();
        
        for (Epic epic : epics) {
            epicNames.add(epic.getName());
        }

        Assert.assertEquals(2, epics.size());
        Assert.assertTrue(epicNames.contains(firstTopLevelEpicName));
        Assert.assertTrue(epicNames.contains(secondTopLevelEpicName));
        Assert.assertFalse(epicNames.contains(childEpicName));
    }

    @Test
    public void getTrackedEpicsByMultipleProjects() {
        final String firstTopLevelEpicName = "first top level epic";
        final String secondTopLevelEpicName = "second top level epic";
        final String thirdTopLevelEpicName = "third top level epic";
        final String childEpicName = "child epic";

        final Project project1 = getSandboxProject();
        Epic topLevelEpic = getEntityFactory().createEpic(firstTopLevelEpicName, project1);
        Epic childEpic = getEntityFactory().createChildEpic(topLevelEpic);
        childEpic.setName(childEpicName);
        childEpic.save();

        newSandboxProject();
        final Project project2 = getSandboxProject();
        getEntityFactory().createEpic(secondTopLevelEpicName, project2);

        newSandboxProject();
        final Project project3 = getSandboxProject();
        getEntityFactory().createEpic(thirdTopLevelEpicName, project3);

        resetInstance();

        Collection<Project> projects = new ArrayList<Project>();
        projects.add(project1);
        projects.add(project2);
        Collection<Epic> epics = getInstance().get().trackedEpics(projects);
        Collection<String> epicNames = new ArrayList<String>();
        
        for (Epic epic : epics) {
            epicNames.add(epic.getName());
        }
        
        Assert.assertEquals(2, epics.size());
        Assert.assertTrue(epicNames.contains(firstTopLevelEpicName));
        Assert.assertTrue(epicNames.contains(secondTopLevelEpicName));
        Assert.assertFalse(epicNames.contains(childEpicName));
        Assert.assertFalse(epicNames.contains(thirdTopLevelEpicName));

        projects.add(project3);
        epics = getInstance().get().trackedEpics(projects);
        epicNames.clear();
        
        for (Epic epic : epics) {
            epicNames.add(epic.getName());
        }

        Assert.assertEquals(3, epics.size());
        Assert.assertTrue(epicNames.contains(firstTopLevelEpicName));
        Assert.assertTrue(epicNames.contains(secondTopLevelEpicName));
        Assert.assertFalse(epicNames.contains(childEpicName));
        Assert.assertTrue(epicNames.contains(thirdTopLevelEpicName));
    }
}
