package com.versionone.om.tests;

import com.versionone.Oid;
import com.versionone.om.Member;
import com.versionone.om.RegressionPlan;
import com.versionone.om.filters.RegressionPlanFilter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RegressionPlanFilterTester extends BaseSDKTester {
    private final String regressionPlanName = "regression plan";

    /*
    @After
    public void after() {
        getSandboxProject().delete();
    }
    */

    @Test
    public void getRegressionPlanWithNullFilterTest() {
        Collection<RegressionPlan> plans = getInstance().get().regressionPlans(null);
        RegressionPlan plan = getEntityFactory().createRegressionPlan(regressionPlanName, getSandboxProject());
        Collection<RegressionPlan> updatedPlans = getInstance().get().regressionPlans(null);

        Assert.assertEquals(1, updatedPlans.size() - plans.size());
        ListAssert.contains(plan, updatedPlans);
    }

    @Test
    public void getRegressionPlanWithValuableFilterTest() throws Exception {
        Member owner = getEntityFactory().createMember("Paul");
        Member nobody = getEntityFactory().createMember("Mike");

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Owner", Oid.fromToken(owner.getID().toString(),
                                              getInstance().getApiClient().getMetaModel()));

        RegressionPlan plan = getEntityFactory().createRegressionPlan(regressionPlanName,
                                                                      getSandboxProject(), attributes);

        RegressionPlanFilter filter = new RegressionPlanFilter();
        filter.project.add(getSandboxProject());
        filter.owners.add(owner);
        Collection<RegressionPlan> plans = getInstance().get().regressionPlans(filter);
        Assert.assertEquals(1, plans.size());
        ListAssert.contains(plan, plans);

        filter = new RegressionPlanFilter();
        filter.project.add(getSandboxProject());
        filter.owners.add(nobody);
        plans = getInstance().get().regressionPlans(filter);
        Assert.assertEquals(0, plans.size());
    }
}
