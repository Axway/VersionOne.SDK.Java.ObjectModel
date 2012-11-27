/*(c) Copyright 2012, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.DB;
import com.versionone.om.*;
import com.versionone.om.filters.EpicFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Epic tester
 */
public class EpicTester extends BaseSDKTester {
    private final Double DELTA = 0.000001;

    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @Test
    public void createEpic() {
        final String epicName = "new epic for testing " + new Date().getTime();
        final String epicDescription = "new epic description " + new Date().getTime();
        final String requestedBy = "Requested by";
        final String reference = "Reference";
        final String epicStatus = "Future";

        Epic epic = createEpicInSandboxProject(epicName);
        epic.setDescription(epicDescription);
        epic.setRequestedBy(requestedBy);
        epic.getStatus().setCurrentValue(epicStatus);
        epic.setReference(reference);

        epic.save();

        AssetID epicId = epic.getID();

        resetInstance();

        Epic actualEpic = getInstance().get().epicByID(epicId);

        Assert.assertEquals(epicName, actualEpic.getName());
        Assert.assertEquals(epicDescription, actualEpic.getDescription());
        Assert.assertEquals(requestedBy, actualEpic.getRequestedBy());
        Assert.assertEquals(epicStatus, actualEpic.getStatus().getCurrentValue());
        Assert.assertEquals(reference, actualEpic.getReference());
        Assert.assertTrue(actualEpic.isActive());
        Assert.assertTrue(actualEpic.canDelete());
        Assert.assertTrue(actualEpic.canGenerateChildEpic());
        Assert.assertTrue(actualEpic.canGenerateChildStory());
    }

    private Epic createEpicInSandboxProject(String epicName) {
        return getEntityFactory().createEpic(epicName, getSandboxProject());
    }

    @Test
    public void deleteEpic() {
        final String epicName = "new epic for testing " + new Date().getTime();

        Epic epic = getInstance().create().epic(epicName, getSandboxProject());
        AssetID epicId = epic.getID();

        resetInstance();

        Epic actualEpic = getInstance().get().epicByID(epicId);
        Assert.assertNotNull(actualEpic);
        actualEpic.delete();

        resetInstance();

        actualEpic = getInstance().get().epicByID(epicId);
        Assert.assertNull(actualEpic);
    }

    @Test
    public void createEpicWithAttributes() {
        final String name = "EpicName";
        final String description = "Test for Epic creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        Epic epic = getEntityFactory().createEpic(name, getSandboxProject(), attributes);
        AssetID epicID = epic.getID();

        resetInstance();

        epic = getInstance().get().epicByID(epicID);

        Assert.assertEquals(name, epic.getName());
        Assert.assertEquals(description, epic.getDescription());
    }

    @Test
    public void checkEpicStatusChanges() {
        Epic epic = createEpicInSandboxProject("Disposable epic");
        epic.setDescription("We're checking if we can normally interact with closed entity");
        epic.save();

        final String token = epic.getID().getToken();

        Epic persistentEpic = getInstance().get().epicByID(AssetID.fromToken(token));
        Assert.assertEquals(true, persistentEpic.isActive());
        Assert.assertEquals(false, persistentEpic.isClosed());

        epic.close();

        persistentEpic = getInstance().get().epicByID(AssetID.fromToken(token));
        Assert.assertEquals(false, persistentEpic.isActive());
        Assert.assertEquals(true, persistentEpic.isClosed());
    }

    @Test
    public void blockingIssue() {
        Issue issue = getEntityFactory().create(new EntityFactory.IEntityCreator<Issue>() {
            public Issue create() {
                return getInstance().create().issue("Test issue", getSandboxProject());
            }
        });
        Epic epic = createEpicInSandboxProject("test epic");
        epic.getBlockingIssues().add(issue);
        epic.save();

        resetInstance();

        Epic actualEpic = getInstance().get().epicByID(epic.getID());
        Assert.assertEquals(1, actualEpic.getBlockingIssues().size());
        ListAssert.contains(issue, actualEpic.getBlockingIssues());
    }

    @Test
    @Ignore("MorphedFrom doesn't contain story which was used to make epic.")
    public void morphedFrom() {
        String storyName = "Story test" + DB.DateTime.now();
        Story story = getEntityFactory().createStory(storyName, getSandboxProject());
        story.breakdown();

        resetInstance();

        EpicFilter filter = new EpicFilter();
        filter.name.add(storyName);
        Collection<Epic> epics = getInstance().get().epics(filter);

        Assert.assertEquals(1, epics.size());
        Epic epic = first(epics);
        //Assert.assertEquals(story, epic.getMorphedFrom());

        epic.delete();
    }

    @Test
    public void swag() {
        final double swag = 10.5;
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Swag", swag);
        Epic epic = getEntityFactory().createEpic("Epic test", getSandboxProject(), attributes);

        resetInstance();

        Epic actualEpic = getInstance().get().epicByID(epic.getID());
        Assert.assertEquals(swag, actualEpic.getSwag(), DELTA);
    }

    @Test
    public void nullSwag() {
        Epic epic = createEpicInSandboxProject("Epic test");
        epic.setSwag(null);
        epic.save();

        resetInstance();

        Epic actualEpic = getInstance().get().epicByID(epic.getID());
        Assert.assertEquals(null, actualEpic.getSwag());
    }

    @Test
    public void value() {
        final double value = 11.2;
        Epic epic = createEpicInSandboxProject("Epic test");
        epic.setValue(value);
        epic.save();

        Epic actualEpic = getInstance().get().epicByID(epic.getID());
        Assert.assertEquals(value, actualEpic.getValue(), DELTA);
    }

    @Test
    public void nullValue() {
        Epic epic = createEpicInSandboxProject("Epic test");
        epic.setValue(null);
        epic.save();

        Epic actualEpic = getInstance().get().epicByID(epic.getID());
        Assert.assertEquals(null, actualEpic.getValue());
    }

    @Test
    public void defaultRankOrder() {
        Epic epic1 = createEpicInSandboxProject("Epic test 1");
        Epic epic2 = createEpicInSandboxProject("Epic test 2");
        Assert.assertTrue(epic1.getRankOrder().isAbove(epic2));
    }

    @Test
    public void updateRankOrder() {
        Epic epic1 = createEpicInSandboxProject("Epic test 1");
        Epic epic2 = createEpicInSandboxProject("Epic test 2");

        epic1.getRankOrder().setBelow(epic2);
        epic1.save();

        Assert.assertTrue(epic2.getRankOrder().isAbove(epic1));
    }

    @Test
    public void canGenerateChildEpic() {
        Epic epic = createEpicInSandboxProject("Epic test");
        Assert.assertTrue(epic.canGenerateChildEpic());
    }

    @Test
    public void cannotGenerateChildEpic() {
        Epic epic = createEpicInSandboxProject("Epic test");
        epic.close();
        Assert.assertFalse(epic.canGenerateChildEpic());
    }

    @Test
    public void canGenerateChildStory() {
        Epic epic = createEpicInSandboxProject("Epic test");
        Assert.assertTrue(epic.canGenerateChildStory());
    }

    @Test
    public void cannotGenerateChildStory() {
        Epic epic = createEpicInSandboxProject("Epic test");
        epic.close();
        Assert.assertFalse(epic.canGenerateChildStory());
    }

    @Test
    public void superEpic() {
        final Epic epic1 = createEpicInSandboxProject("Epic test 1");
        Epic subEpic = getEntityFactory().create(new EntityFactory.IEntityCreator<Epic>() {
            public Epic create() {
                return epic1.generateChildEpic();
            }
        });

        Assert.assertEquals(epic1, subEpic.getSuper());
    }

    @Test
    public void risk() {
        final double value = 5.4;
        Epic epic = createEpicInSandboxProject("Epic test");
        epic.setRisk(value);
        epic.save();

        Epic actualEpic = getInstance().get().epicByID(epic.getID());
        Assert.assertEquals(value, actualEpic.getRisk(), DELTA);
    }

    @Test(expected=DataException.class)
    public void riskGreaterThanUpperLimit() {
        final double value = 10.5;
        Epic epic = createEpicInSandboxProject("Epic test");
        epic.setRisk(value);
        epic.save();
    }

    @Test(expected=DataException.class)
    public void riskLessThanLowerLimit() {
        final double value = -0.4;
        Epic epic = createEpicInSandboxProject("Epic test");
        epic.setRisk(value);
        epic.save();
    }

    @Test
    public void testAsEpicChildren() {
        Epic epic = getEntityFactory().createEpic("Epic test 1", getSandboxProject());
        com.versionone.om.Test test = getEntityFactory().createTest("test for epic", epic);

        resetInstance();

        Epic actualEpic = getInstance().get().epicByID(epic.getID());
        Assert.assertEquals(test, first(actualEpic.getChildTests(null)));
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }
}
