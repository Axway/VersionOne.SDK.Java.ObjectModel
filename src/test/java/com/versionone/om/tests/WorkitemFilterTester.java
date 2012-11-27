package com.versionone.om.tests;

import com.versionone.apiclient.FilterTerm;
import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.filters.DefectFilter;
import com.versionone.om.filters.StoryFilter;

import java.util.Collection;
import java.util.HashMap;

public class WorkitemFilterTester extends PrimaryWorkitemFilterTesterBase {
    private DefectFilter getFilter() {
        DefectFilter filter = new DefectFilter();
        filter.project.add(sandboxProject);
        return filter;
    }

    @Test
    //TODO this test throws exception, but manually url is working
    public void noDetailEstimate() {
        getEntityFactory().createStory("Story 1", getSandboxProject());

        StoryFilter filter = new StoryFilter();
        filter.detailEstimate.addTerm(FilterTerm.Operator.Equal, 1.0);
        Assert.assertEquals(0, getInstance().get().story(filter).size());
    }

    @Test
    public void detailEstimate() {
        Story story = getEntityFactory().createStory("Story 1", getSandboxProject());
        Task task = getEntityFactory().createTask("Task 1", story, new HashMap<String, Object>());
        task.setDetailEstimate(18.0);
        task.save();

        StoryFilter filter = new StoryFilter();
        filter.detailEstimate.addTerm(FilterTerm.Operator.Equal, 1.0);
        Collection<Story> stories = getInstance().get().story(filter);
        Assert.assertEquals(0, stories.size());

        filter = new StoryFilter();
        filter.detailEstimate.addTerm(FilterTerm.Operator.NotExists);
        stories = getInstance().get().story(filter);
        ListAssert.contains(story, stories);
    }

    @Test
    public void reference() {
        DefectFilter filter = getFilter();
        filter.reference.add(REFERENCE);
        Assert.assertEquals(1, getInstance().get().defects(filter).size());
    }

	@Test
	public void noThemeAmongWorkitems() {
		Theme theme = getEntityFactory().create( new EntityFactory.IEntityCreator<Theme>() {
            public Theme create() {
                return getInstance().create().theme("A Theme", getSandboxProject());
            }
        });

		resetInstance();

		ListAssert.notcontains(theme.getName(), getInstance().get().workitems(null), new EntityToNameTransformer<Workitem>());
	}

	@Test
	public void noEpicAmongWorkitems() {
		Epic epic = getEntityFactory().createEpic("War and Piece", getSandboxProject());

		resetInstance();

		ListAssert.contains(epic.getName(), getInstance().get().workitems(null), new EntityToNameTransformer<Workitem>(), "Can't find created epic.");
	}
}
