package com.versionone.om.tests;

import com.versionone.apiclient.FilterTerm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.versionone.om.Epic;
import com.versionone.om.Member;
import com.versionone.om.Project;
import com.versionone.om.Story;
import com.versionone.om.filters.EpicFilter;

import java.util.Collection;

public class EpicFilterTester extends BaseSDKTester {
    private static final String HIGH = "High";
    private static final String MEDIUM = "Medium";
    private static final String FEATURE = "New Feature";
    private static final String FIND_ME = "Find Me";
    private static final String SALES = "Sales";
    private Project sandboxProject;
    private Member andre;
	private Epic epic1;
    private Epic epic2;

    private EpicFilter getFilter() {
        EpicFilter filter = new EpicFilter();
        filter.project.add(sandboxProject);
        return filter;
    }

    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Before
    public void createAssets() {
        newSandboxProject();
        sandboxProject = getSandboxProject();

        andre = getEntityFactory().createMember("Member:1000");

        epic1 = getEntityFactory().createEpic("Epic 1", getSandboxProject());
        epic2 = getEntityFactory().createEpic("Epic 2", getSandboxProject());

        Epic childEpic = getEntityFactory().createChildEpic(epic2);
        childEpic.setName("Son of an Epic");
        childEpic.save();

        epic1.getOwners().add(andre);
        epic1.getSource().setCurrentValue("Customer");
        epic1.setRisk(5.0);
        epic1.setValue(20.0);
        epic1.setSwag(20.0);
        epic1.getType().setCurrentValue("New Feature");
        epic1.save();

        epic2.getPriority().setCurrentValue("High");
        epic2.getSource().setCurrentValue("Sales");
        epic2.setReference(FIND_ME);
        epic2.setRisk(10.0);
        epic2.setValue(80.0);
        epic2.setSwag(30.0);
        epic2.save();

        Story story1 = getEntityFactory().create( new EntityFactory.IEntityCreator<Story>() {
            public Story create() {
                return epic1.generateChildStory();
            }
        });
        story1.setReference(FIND_ME);
        story1.setCustomer(andre);
        story1.save();
    }

    @Test
    public void source() {
        EpicFilter filter = getFilter();
        filter.source.add(SALES);
        Assert.assertEquals(1, getInstance().get().epics(filter).size());
    }

    @Test
    public void reference() {
        EpicFilter filter = getFilter();
        filter.reference.add(FIND_ME);
        Assert.assertEquals(1, getInstance().get().epics(filter).size());
    }

    @Test
    public void type() {
        EpicFilter filter = getFilter();
        filter.type.add(FEATURE);
        Assert.assertEquals(1, getInstance().get().epics(filter).size());
    }

    @Test
    public void risk() {
        EpicFilter filter = getFilter();
        filter.risk.addTerm(FilterTerm.Operator.Equal, 5.0);
        Collection<Epic> epics = getInstance(). get().epics(filter);
        Assert.assertEquals(1, epics.size());
        ListAssert.contains(epic1, epics);

        filter = getFilter();
        filter.risk.range(1.0, 4.0);
        Assert.assertEquals(0, getInstance(). get().epics(filter).size());

        filter = getFilter();
        filter.risk.range(6.0, 9.0);
        Assert.assertEquals(0, getInstance(). get().epics(filter).size());

        filter = getFilter();
        filter.risk.range(1.0, 5.5);
        epics = getInstance(). get().epics(filter);
        Assert.assertEquals(1, epics.size());
        ListAssert.contains(epic1, epics);

        filter = getFilter();
        filter.risk.range(1.0, 10.0);
        epics = getInstance(). get().epics(filter);
        Assert.assertEquals(2, epics.size());
        ListAssert.contains(epic1, epics);
        ListAssert.contains(epic2, epics);
    }

    @Test
    public void value() {
        EpicFilter filter = getFilter();
        filter.value.addTerm(FilterTerm.Operator.Equal, 20.0);
        Collection<Epic> epics = getInstance(). get().epics(filter);
        Assert.assertEquals(1, epics.size());
        ListAssert.contains(epic1, epics);

        filter = getFilter();
        filter.value.range(1.0, 19.0);
        Assert.assertEquals(0, getInstance(). get().epics(filter).size());

        filter = getFilter();
        filter.value.addTerm(FilterTerm.Operator.GreaterThan, 20.05);
        epics = getInstance(). get().epics(filter);
        Assert.assertEquals(1, epics.size());
        ListAssert.contains(epic2, epics);

        filter = getFilter();
        filter.value.range(15.0, 25.0);
        epics = getInstance(). get().epics(filter);
        Assert.assertEquals(1, epics.size());
        ListAssert.contains(epic1, epics);

        filter = getFilter();
        filter.value.range(1.0, 100.0);
        epics = getInstance(). get().epics(filter);
        Assert.assertEquals(2, epics.size());
        ListAssert.contains(epic1, epics);
        ListAssert.contains(epic2, epics);
    }

    @Test
    public void swag() {
        EpicFilter filter = getFilter();
        filter.swag.addTerm(FilterTerm.Operator.Equal, 20.0);
        Collection<Epic> epics = getInstance(). get().epics(filter);
        Assert.assertEquals(1, epics.size());
        ListAssert.contains(epic1, epics);

        filter = getFilter();
        filter.swag.range(1.0, 19.0);
        Assert.assertEquals(0, getInstance(). get().epics(filter).size());

        filter = getFilter();
        filter.swag.addTerm(FilterTerm.Operator.GreaterThan, 20.05);
        epics = getInstance(). get().epics(filter);
        Assert.assertEquals(1, epics.size());
        ListAssert.contains(epic2, epics);

        filter = getFilter();
        filter.swag.range(15.0, 25.0);
        epics = getInstance(). get().epics(filter);
        Assert.assertEquals(1, epics.size());
        ListAssert.contains(epic1, epics);

        filter = getFilter();
        filter.swag.range(1.0, 100.0);
        epics = getInstance(). get().epics(filter);
        Assert.assertEquals(2, epics.size());
        ListAssert.contains(epic1, epics);
        ListAssert.contains(epic2, epics);
    }

    @Test
    public void priority() {
        EpicFilter filter = getFilter();
        filter.priority.add(HIGH);
        Assert.assertEquals(1, getInstance().get().epics(filter).size());
    }

    @Test
    public void getChildEpicsWithNullFilter() {
        Epic epic = getInstance().get().epicByID(epic2.getID());
        Assert.assertEquals(1, epic.getChildEpics(null).size());
    }

	@Test
	public void noStoryAmongEpics() {
		Story decoy = getEntityFactory().createStory("Decoy", getSandboxProject());

		resetInstance();

		ListAssert.notcontains(decoy.getName(), getInstance().get().epics(null), new EntityToNameTransformer<Epic>());
	}

    @Test
    public void owners() {
        EpicFilter filter = getFilter();
        filter.owners.add(andre);
        Assert.assertEquals(1, getInstance().get().epics(filter).size());
    }

    @Test
    public void activeState() {
        Epic epic = getEntityFactory().createEpic("Some new Epic", getSandboxProject());

        EpicFilter filter = getFilter();
        filter.getState().add(EpicFilter.State.Active);
        Collection<Epic> epics = getInstance().get().epics(filter);

        ListAssert.contains(epic, epics);
    }

    @Test
    public void closedState() {
        Epic epic = getEntityFactory().createEpic("Some new Epic", getSandboxProject());
        Epic epic1 = getEntityFactory().createEpic("Another Epic", getSandboxProject());
        epic1.close();

        EpicFilter filter = getFilter();
        filter.getState().add(EpicFilter.State.Closed);
        Collection<Epic> epics = getInstance().get().epics(filter);

        ListAssert.notcontains(epic, epics);
        ListAssert.contains(epic1, epics);
    }

    @Test
    public void bothStates() {
        Epic epic = getEntityFactory().createEpic("Some new Epic", getSandboxProject());
        Epic epic1 = getEntityFactory().createEpic("Another Epic", getSandboxProject());
        epic1.close();

        EpicFilter filter = getFilter();
        Collection<Epic> epics = getInstance().get().epics(filter);

        ListAssert.contains(epic, epics);
        ListAssert.contains(epic1, epics);
    }

    @Test
    public void deletedState() {
        Epic epic = getEntityFactory().createEpic("Some new Epic", getSandboxProject());
        epic.delete();

        EpicFilter filter = getFilter();
        Collection<Epic> epics = getInstance().get().epics(filter);

        ListAssert.notcontains(epic, epics);
    }
}
