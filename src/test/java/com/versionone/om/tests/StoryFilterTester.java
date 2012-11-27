package com.versionone.om.tests;

import com.versionone.apiclient.APIException;
import com.versionone.apiclient.ConnectionException;
import com.versionone.apiclient.FilterTerm;
import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.OidException;
import com.versionone.apiclient.Query;
import com.versionone.om.Epic;
import com.versionone.om.Member;
import com.versionone.om.Retrospective;
import com.versionone.om.Story;
import com.versionone.om.filters.BaseAssetFilter;
import com.versionone.om.filters.StoryFilter;

import org.junit.Assert;
import org.junit.Ignore;

import static org.junit.Assert.fail;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StoryFilterTester extends PrimaryWorkitemFilterTesterBase {

    private StoryFilter getFilter() {
        StoryFilter filter = new StoryFilter();
        filter.project.add(sandboxProject);
        return filter;
    }

    @Test
    public void testProject() {
        Collection<Story> stories = getInstance().get().story(
                getFilter());

        for (Story result : stories) {
            Assert.assertEquals(getSandboxProject(), result.getProject());
        }
    }

    @Test
    public void testNoOwner() {
        StoryFilter filter = getFilter();
        filter.owners.add(null);

        Collection<Story> stories = getInstance().get().story(filter);

        for (Story result : stories) {
            if (result.getOwners().size() > 0) {
                fail("Filtered Query should only return stories owned by no one.");
            }
        }
    }

    @Test
    public void testFilterExist() throws ConnectionException, APIException, OidException {
        IAssetType assetType = getInstance().getApiClient().getMetaModel().getAssetType("Story");

        FilterTerm customFilter = new FilterTerm(assetType.getAttributeDefinition("Timebox"));
        customFilter.exists();

        Query query = new Query(assetType);
        query.setFilter(customFilter);
        getInstance().getApiClient().getServices().retrieve(query);
    }

    @Test
    public void testNoOrAndreOwner() {
        StoryFilter filter = getFilter();
        filter.owners.add(null);
        filter.owners.add(andre);

        Collection<Story> stories = getInstance().get().story(filter);

        for (Story result : stories) {
            if (!findRelated(andre, result.getOwners())
                    && (result.getOwners().size() > 0)) {
                fail("Filtered Query should only return stories owned by "
                        + andre.getName() + " or no one.");
            }
        }
    }

    @Test
    public void testNames() {
        StoryFilter filter = getFilter();
        filter.name.add("Defect 2");
        filter.name.add("Story 2");

        Collection<Story> stories = getInstance().get().story(filter);

        Assert.assertEquals(1, stories.size());
    }

    @Test
    public void testDispalyIDs() {
        StoryFilter filter = getFilter();
        filter.displayID.add(story1.getDisplayID());
        filter.displayID.add(defect1.getDisplayID());

        Collection<Story> stories = getInstance().get().story(filter);

        Assert.assertEquals(1, stories.size());
    }

    @Test
    public void testState() {
        Collection<Story> stories = getInstance().get().story(
                getFilter());

        int allStoriesCount = stories.size();

        Story closedStory = sandboxProject.createStory("Close Me");
        closedStory.close();

        Assert.assertEquals(++allStoriesCount, getInstance().get()
                .story(getFilter()).size());

        StoryFilter openFilter = getFilter();
        openFilter.getState().add(BaseAssetFilter.State.Active);
        Collection<Story> activeStories = getInstance().get().story(
                openFilter);
        Assert.assertEquals(allStoriesCount - 1, activeStories.size());
        for (Story story : activeStories) {
            Assert.assertTrue(story.isActive());
        }

        StoryFilter closedFilter = getFilter();
        closedFilter.getState().add(BaseAssetFilter.State.Closed);
        Collection<Story> closedStories = getInstance().get().story(
                closedFilter);
        Assert.assertEquals(1, closedStories.size());
        for (Story story : closedStories) {
            Assert.assertTrue(story.isClosed());
        }
    }

    @Test
    public void testRequestedBy() {
        final String strMe = "ME";
        Story story = getSandboxProject().createStory("RequestdBy Filter");

        story.setRequestedBy(strMe);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());

        StoryFilter filter = new StoryFilter();
        filter.requestedBy.add(strMe);

        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(story, results));
        for (Story result : results) {
            Assert.assertEquals(strMe, result.getRequestedBy());
        }
    }

    @Test
    public void testBuild() {
        final String strBuildNumber = "10.2.24.1";
        Story story = getSandboxProject().createStory("Build Filter");

        story.setBuild(strBuildNumber);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());

        StoryFilter filter = getFilter();
        filter.build.add(strBuildNumber);

        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(story, results));
        for (Story result : results) {
            Assert.assertEquals(strBuildNumber, result.getBuild());
        }
    }

    @Test
    public void testEpic() {
        Epic epic = getInstance().create().epic("Filter by me",
                getSandboxProject());
        Story story = epic.generateChildStory();
        story.setName("Find Me");
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        epic = getInstance().get().epicByID(epic.getID());

        StoryFilter filter = getFilter();
        filter.epic.add(epic);
        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(story, results));
        for (Story result : results) {
            Assert.assertEquals(epic, result.getEpic());
        }
    }

    @Test
    public void testRisk() {
        Story story = getSandboxProject().createStory("Risk Filter");
        String riskValue = story.getRisk().getAllValues()[0];
        story.getRisk().setCurrentValue(riskValue);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());

        StoryFilter filter = getFilter();
        filter.risk.add(riskValue);

        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(story, results));
        for (Story result : results) {
            Assert.assertEquals(riskValue, result.getRisk().getCurrentValue());
        }
    }

    @Test
    public void testType() {
        Story story = getSandboxProject().createStory("Type Filter");
        String typeValue = story.getType().getAllValues()[0];
        story.getType().setCurrentValue(typeValue);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());

        StoryFilter filter = getFilter();
        filter.type.add(typeValue);

        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(story, results));
        for (Story result : results) {
            Assert.assertEquals(typeValue, result.getType().getCurrentValue());
        }
    }

    @Test
    public void testCustomer() {
        Member customer = null;
        for (Member member : getInstance().getMembers()) {
            customer = member;
            break;
        }

        Story story = getSandboxProject().createStory("Customer filter");
        story.setCustomer(customer);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        customer = getInstance().get().memberByID(customer.getID());

        StoryFilter filter = getFilter();
        filter.customer.add(customer);

        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(story, results));
        for (Story result : results) {
            Assert.assertEquals(customer, result.getCustomer());
        }
    }

    @Test
    public void testDependsOnStories() {
        Story benefactor = getSandboxProject().createStory("Benefactor");
        Story dependant = getSandboxProject().createStory("Dependant");
        dependant.getDependsOnStories().add(benefactor);
        dependant.save();

        resetInstance();
        dependant = getInstance().get().storyByID(dependant.getID());
        benefactor = getInstance().get().storyByID(benefactor.getID());

        StoryFilter filter = getFilter();
        filter.dependsOnStories.add(benefactor);
        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(dependant, results));
        for (Story result : results) {
            Assert.assertTrue(
                    "Expected story to depend on value used in filter",
                    findRelated(benefactor, result.getDependsOnStories()));
        }
    }

    @Test
    public void testDependantStories() {
        Story benefactor = getSandboxProject().createStory("Benefactor");
        Story dependant = getSandboxProject().createStory("Dependant");
        dependant.getDependsOnStories().add(benefactor);
        dependant.save();

        resetInstance();
        dependant = getInstance().get().storyByID(dependant.getID());
        benefactor = getInstance().get().storyByID(benefactor.getID());

        StoryFilter filter = getFilter();
        filter.dependentStories.add(dependant);
        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(benefactor, results));
        for (Story result : results) {
            Assert.assertTrue("Expected story to includ value used in filter in dependant stories",
                    findRelated(dependant, result.getDependentStories()));
        }
    }

    @Test
    public void testIdentifiedIn() {
        Retrospective retro = getSandboxProject().createRetrospective("Has a story");
        Story story = retro.createStory("Retrospective filter");
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        retro = getInstance().get().retrospectiveByID(retro.getID());

        StoryFilter filter = getFilter();
        filter.identifiedIn.add(retro);

        Collection<Story> results = getSandboxProject().getStories(filter);

        Assert.assertTrue("Expected to find story that matched filter.",
                findRelated(story, results));
        for (Story result : results) {
            Assert.assertEquals(retro, result.getIdentifiedIn());
        }
    }

    @Test
    @Ignore("DB index text for search not so fast, so this test fails. Requires big delay.")
    public void findInDefaultFields() {
        String nameString = newGuid();
        getSandboxProject().createStory(nameString);

        StoryFilter filter = getFilter();
        filter.find.setSearchString(nameString);

        //we need this sleep to let DB index string for search.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals(1, getInstance().get().story(filter).size());
    }
    
    @Test
    @Ignore("Server throws NotSupportedException: SimpleLongTextAttributeDefinition.BuildPredicate (Story.Description)")
    public void filterByDescription() {
        String weirdString = newGuid();

        Story story = getEntityFactory().createStory("my test story for description", getSandboxProject());
        story.setDescription(weirdString);
        
        resetInstance();
        
        StoryFilter filter = new StoryFilter();
        filter.description.add(weirdString);

        //we need this sleep to let DB index string for search.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        Assert.assertEquals(1, getInstance().get().story(filter).size());
    }

    @Test
    @Ignore("DB index text for search not so fast, so this test fails. Requires big delay.")
    public void findInDescriptionField() {
        String weirdString = newGuid();
        Story a = getSandboxProject().createStory("Has a weird description");
        a.setDescription(weirdString);
        Story b = getSandboxProject().createStory("Also with funky data");
        b.setDescription(weirdString);

        a.save();
        b.save();

        StoryFilter filter = getFilter();
        filter.find.setSearchString(weirdString);
        filter.find.fields.add("Description");

        //we need this sleep to let DB index string for search.
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {}

        Assert.assertEquals(2, getInstance().get().story(filter).size());
    }

    @Test
    public void testFindInDescriptionNotFound() {
        StoryFilter filter = getFilter();
        filter.find.setSearchString(newGuid());
        filter.find.fields.add("Description");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {}

        Assert.assertEquals(0, getInstance().get().story(filter).size());
    }

    @Test
    public void testNoProjectAmongStories() {
        String sandboxName = getSandboxProject().getName();

        resetInstance();

        ListAssert.notcontains(sandboxName,
                getInstance().get().baseAssets(new StoryFilter()),
                new EntityToNameTransformer<Story>());
    }

    @Test
    public void testNoEpicAmongStories() {
        Epic epic = getSandboxProject().createEpic("War and Piece");

        resetInstance();

        ListAssert.notcontains(epic.getName(), getInstance().get().story(null), new EntityToNameTransformer<Story>());
    }
}
