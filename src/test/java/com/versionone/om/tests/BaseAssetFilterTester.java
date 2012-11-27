package com.versionone.om.tests;

import com.versionone.DB;
import com.versionone.om.BaseAsset;
import com.versionone.om.Project;
import com.versionone.om.Story;
import com.versionone.om.Task;
import com.versionone.om.filters.BaseAssetFilter;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;

import static com.versionone.apiclient.FilterTerm.Operator.*;

public class BaseAssetFilterTester extends BaseSDKTester {
    @Test
    public void findUnknownEntityType() {
        BaseAssetFilter filter = new BaseAssetFilter();

        filter.find.setSearchString(newGuid());
        Collection<BaseAsset> assets = getInstance().get().baseAssets(filter);
        Assert.assertEquals(0, assets.size());
    }

    @Test
    public void changeDate() throws InterruptedException {
        final Project project = getSandboxProject();
        final Story story = getEntityFactory().createStory("Story1", project);
        Thread.sleep(1000);
        final Task task1 = getEntityFactory().createTask("Task1.1", story, new HashMap<String, Object>());
        Thread.sleep(1000);
        final Task task2 = getEntityFactory().createTask("Task1.2", story, new HashMap<String, Object>());
        final DB.DateTime task1Date = task1.getCreateDate().convertLocalToUtc();
        resetInstance();

        BaseAssetFilter filter = new BaseAssetFilter();
        filter.changeDateUtc.addTerm(GreaterThan, task1Date);
        Collection<BaseAsset> baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.notcontains(project, baseAssets);
        ListAssert.notcontains(story, baseAssets);
        ListAssert.notcontains(task1, baseAssets);
        ListAssert.contains(task2, baseAssets);

        filter.changeDateUtc.clear();
        filter.changeDateUtc.addTerm(GreaterThanOrEqual, task1Date);
        baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.notcontains(project, baseAssets);
        ListAssert.notcontains(story, baseAssets);
        ListAssert.contains(task1, baseAssets);
        ListAssert.contains(task2, baseAssets);

        filter.changeDateUtc.clear();
        filter.changeDateUtc.addTerm(LessThan, task1Date);
        baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.contains(project, baseAssets);
        ListAssert.contains(story, baseAssets);
        ListAssert.notcontains(task1, baseAssets);
        ListAssert.notcontains(task2, baseAssets);

        filter.changeDateUtc.clear();
        filter.changeDateUtc.addTerm(LessThanOrEqual, task1Date);
        baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.contains(project, baseAssets);
        ListAssert.contains(story, baseAssets);
        ListAssert.contains(task1, baseAssets);
        ListAssert.notcontains(task2, baseAssets);

        story.delete();
        project.delete();
    }

    @Test
    public void createDate() throws InterruptedException {
        final Project project = getSandboxProject();
        final Story story = getEntityFactory().createStory("Story1", project);
        Thread.sleep(1000);
        final Task task1 = getEntityFactory().createTask("Task1.1", story, new HashMap<String, Object>());
        Thread.sleep(1000);
        final Task task2 = getEntityFactory().createTask("Task1.2", story, new HashMap<String, Object>());
        final DB.DateTime task1Date = task1.getCreateDate().convertLocalToUtc();
        resetInstance();

        BaseAssetFilter filter = new BaseAssetFilter();
        filter.createDateUtc.addTerm(GreaterThan, task1Date);
        Collection<BaseAsset> baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.notcontains(project, baseAssets);
        ListAssert.notcontains(story, baseAssets);
        ListAssert.notcontains(task1, baseAssets);
        ListAssert.contains(task2, baseAssets);

        filter.createDateUtc.clear();
        filter.createDateUtc.addTerm(GreaterThanOrEqual, task1Date);
        baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.notcontains(project, baseAssets);
        ListAssert.notcontains(story, baseAssets);
        ListAssert.contains(task1, baseAssets);
        ListAssert.contains(task2, baseAssets);

        filter.createDateUtc.clear();
        filter.createDateUtc.addTerm(LessThan, task1Date);
        baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.contains(project, baseAssets);
        ListAssert.contains(story, baseAssets);
        ListAssert.notcontains(task1, baseAssets);
        ListAssert.notcontains(task2, baseAssets);

        filter.createDateUtc.clear();
        filter.createDateUtc.addTerm(LessThanOrEqual, task1Date);
        baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.contains(project, baseAssets);
        ListAssert.contains(story, baseAssets);
        ListAssert.contains(task1, baseAssets);
        ListAssert.notcontains(task2, baseAssets);

        story.delete();
        project.delete();
    }

    @Test
    public void filterByName() {
        final String name = "English story";

        final Project project = getSandboxProject();
        final Story story = getEntityFactory().createStory(name, project);
        final Story story2 = getEntityFactory().createStory(name + '2', project);
        resetInstance();

        BaseAssetFilter filter = new BaseAssetFilter();
        filter.name.add(name);
        Collection<BaseAsset> assets = getInstance().get().baseAssets(filter);
        ListAssert.notcontains(project, assets);
        ListAssert.contains(story, assets);
        ListAssert.notcontains(story2, assets);
    }

    @Test
    @Ignore("Doesn't support filter by russian characters.")
    public void filterByInternationalName() {
        final String name = "\u0420\u0443\u0441 - Русская история";

        final Project project = getSandboxProject();
        final Story story = getEntityFactory().createStory(name, project);
        resetInstance();

        BaseAssetFilter filter = new BaseAssetFilter();
        filter.name.add(name);
        Collection<BaseAsset> baseAssets = getInstance().get().baseAssets(filter);
        ListAssert.notcontains(project, baseAssets);
        ListAssert.contains(story, baseAssets);
    }

    @Test
    public void activeState() {
        final Project project = getSandboxProject();
        final Story story = getEntityFactory().createStory("Some story", project);
        resetInstance();

        BaseAssetFilter filter = new BaseAssetFilter();
        filter.getState().add(BaseAssetFilter.State.Active);
        Collection<BaseAsset> baseAssets = getInstance().get().baseAssets(filter);

        ListAssert.contains(story, baseAssets);
    }

    @Test
    public void closedState() {
        final Project project = getSandboxProject();
        final Story story = getEntityFactory().createStory("Some story", project);
        final Story story1 = getEntityFactory().createStory("Another story", project);
        story1.close();
        resetInstance();

        BaseAssetFilter filter = new BaseAssetFilter();
        filter.getState().add(BaseAssetFilter.State.Closed);
        Collection<BaseAsset> baseAssets = getInstance().get().baseAssets(filter);

        ListAssert.notcontains(story, baseAssets);
        ListAssert.contains(story1, baseAssets);
    }

    @Test
    public void bothStates() {
        final Project project = getSandboxProject();
        final Story story = getEntityFactory().createStory("Some story", project);
        final Story story1 = getEntityFactory().createStory("Another story", project);
        story1.close();
        resetInstance();

        BaseAssetFilter filter = new BaseAssetFilter();
        Collection<BaseAsset> baseAssets = getInstance().get().baseAssets(filter);

        ListAssert.contains(story, baseAssets);
        ListAssert.contains(story1, baseAssets);
    }

    @Test
    public void deletedState() {
        final Project project = getSandboxProject();
        final Story story = getEntityFactory().createStory("Some story", project);
        story.delete();
        resetInstance();

        BaseAssetFilter filter = new BaseAssetFilter();
        Collection<BaseAsset> baseAssets = getInstance().get().baseAssets(filter);

        ListAssert.notcontains(story, baseAssets);
    }
}
