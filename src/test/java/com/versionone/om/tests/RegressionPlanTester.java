package com.versionone.om.tests;

import com.versionone.Oid;
import com.versionone.om.Member;
import com.versionone.om.RegressionPlan;

//import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class RegressionPlanTester extends BaseSDKTester {
    private Member member;
    private final String regressionPlanName = "regression plan";
    private final String Description = "description for regression plan";
    private final String Reference = "my reference";

    /*
    @After
    public void after() {
        getSandboxProject().delete();
    }
    */

    @Before
    public void before() {
        member = getEntityFactory().createMember("test user");
    }

    @Test
    public void createRegressionPlanTest() {
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(regressionPlanName,
                                                                                getSandboxProject());
        Assert.assertEquals(regressionPlanName, regressionPlan.getName());
    }

    @Test
    public void createRegressionPlanWithAttributesTest() throws Exception {
        Map<String, Object> attributes = createAttributesWithDescription(regressionPlanName);
        attributes.put("Reference", Reference);
        attributes.put("Owner", Oid.fromToken(member.getID().toString(), getInstance().getApiClient().getMetaModel()));
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(regressionPlanName,
                                                                                getSandboxProject(), attributes);

        Assert.assertEquals(regressionPlanName, regressionPlan.getName());
        Assert.assertEquals(attributes.get("Description"), regressionPlan.getDescription());
        Assert.assertEquals(Reference, regressionPlan.getReference());
        Assert.assertEquals(member, regressionPlan.getOwner());
    }

    @Test
    public void updateRegressionPlanTest() {
        final String addonForName = "updated";
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(regressionPlanName,
                                                                                getSandboxProject());
        resetInstance();

        RegressionPlan regressionPlanNew = getInstance().get().regressionPlanByID(regressionPlan.getID());
        Assert.assertNotNull(regressionPlanNew);
        regressionPlanNew.setName(regressionPlanName + addonForName);
        regressionPlanNew.setDescription(Description);
        regressionPlanNew.setReference(Reference);
        regressionPlanNew.setOwner(member);
        regressionPlanNew.save();

        resetInstance();

        regressionPlan = getInstance().get().regressionPlanByID(regressionPlan.getID());
        Assert.assertEquals(regressionPlanName + addonForName, regressionPlan.getName());
        Assert.assertEquals(Description, regressionPlan.getDescription());
        Assert.assertEquals(Reference, regressionPlan.getReference());
        Assert.assertEquals(member, regressionPlan.getOwner());
    }

    @Test
    public void deleteRegressionPlanTest() {
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(regressionPlanName,
                                                                                getSandboxProject());
        resetInstance();

        RegressionPlan regressionPlanNew = getInstance().get().regressionPlanByID(regressionPlan.getID());
        Assert.assertNotNull(regressionPlanNew);
        regressionPlanNew.delete();

        regressionPlanNew = getInstance().get().regressionPlanByID(regressionPlan.getID());
        Assert.assertNull(regressionPlanNew);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void canNotCloseTest() {
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(regressionPlanName,
                                                                                getSandboxProject());
        Assert.assertTrue(regressionPlan.isActive());
        Assert.assertFalse(regressionPlan.isClosed());

        Assert.assertFalse(regressionPlan.canClose());
        Assert.assertTrue(regressionPlan.canDelete());

        regressionPlan.close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void canNotReactivateTest() {
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(regressionPlanName,
                                                                                getSandboxProject());
        Assert.assertFalse(regressionPlan.canReactivate());
        regressionPlan.reactivate();
    }
}
