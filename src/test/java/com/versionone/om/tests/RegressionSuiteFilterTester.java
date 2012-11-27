package com.versionone.om.tests;

import com.versionone.Oid;
import com.versionone.apiclient.FilterTerm;
import com.versionone.apiclient.OidException;
import com.versionone.om.Member;
import com.versionone.om.RegressionPlan;
import com.versionone.om.RegressionSuite;
import com.versionone.om.filters.RegressionSuiteFilter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RegressionSuiteFilterTester extends BaseSDKTester {
    private RegressionPlan regressionPlan;
    private RegressionPlan regressionPlan2;
    private final String RegressionSuiteName = "Regression Suite";

    @Before
    public void before() {
        regressionPlan = getEntityFactory().createRegressionPlan("Test plan", getSandboxProject());
        regressionPlan2 = getEntityFactory().createRegressionPlan("RegPlanFake", getSandboxProject());
    }

    @Test
    public void getRegressionSuiteWithNullFilter() {
        Collection<RegressionSuite> suites = getInstance().get().regressionSuites(null);
        RegressionSuite suite = getEntityFactory().createRegressionSuite(RegressionSuiteName, regressionPlan);
        Collection<RegressionSuite> updatedSuites = getInstance().get().regressionSuites(null);

        Assert.assertEquals(1, updatedSuites.size() - suites.size());
        ListAssert.contains(suite, updatedSuites);
    }

    @Test
    public void getRegressionSuitesByOwner() throws OidException {
        Member owner = getEntityFactory().createMember("SuiteOwner");
        Member nobody = getEntityFactory().createMember("OtherOwner");

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Owner", Oid.fromToken(owner.getID().toString(),
                                              getInstance().getApiClient().getMetaModel()));
        RegressionSuite suite = getEntityFactory().createRegressionSuite(RegressionSuiteName, regressionPlan,
                                                                        attributes);

        RegressionSuiteFilter filter = new RegressionSuiteFilter();
        filter.owners.add(owner);
        Collection<RegressionSuite> suites = getInstance().get().regressionSuites(filter);
        Assert.assertEquals(1, suites.size());
        ListAssert.contains(suite, suites);

        filter = new RegressionSuiteFilter();
        filter.owners.add(nobody);
        suites = getInstance().get().regressionSuites(filter);
        Assert.assertEquals(0, suites.size());
    }

    @Test
    public void getRegressionSuitesWithSpecificRegressionPlan() throws OidException {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("RegressionPlan", Oid.fromToken(regressionPlan.getID().toString(),
                                                       getInstance().getApiClient().getMetaModel()));
        RegressionSuite suite = getEntityFactory().createRegressionSuite(RegressionSuiteName, regressionPlan,
                                                                        attributes);

        RegressionSuiteFilter filter = new RegressionSuiteFilter();
        filter.regressionPlan.add(regressionPlan);
        Collection<RegressionSuite> suites = getInstance().get().regressionSuites(filter);
        Assert.assertEquals(1, suites.size());
        ListAssert.contains(suite, suites);

        filter = new RegressionSuiteFilter();
        filter.regressionPlan.add(regressionPlan2);
        suites = getInstance().get().regressionSuites(filter);
        Assert.assertEquals(0, suites.size());
    }

    @Test
    public void getRegressionSuitesByPlan() {
        RegressionSuite suite = getEntityFactory().createRegressionSuite(RegressionSuiteName, regressionPlan, null);

        RegressionSuiteFilter filter = new RegressionSuiteFilter();
        filter.regressionPlan.add(regressionPlan);
        Collection<RegressionSuite> suites = getInstance().get().regressionSuites(filter);
        Assert.assertTrue(suites.size() > 0);
        ListAssert.contains(suite, suites);
    }

    @Test
    public void getRegressionSuitesByEstimate() {
        Double estimate = 14.0;

        RegressionPlan plan = getEntityFactory().createRegressionPlan("RegEstimatePlan", getSandboxProject());

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Estimate", estimate);
        RegressionSuite suite = getEntityFactory().createRegressionSuite(RegressionSuiteName, plan, attributes);

        RegressionSuiteFilter filter = new RegressionSuiteFilter();
        filter.estimate.addTerm(FilterTerm.Operator.Equal, estimate);
        Collection<RegressionSuite> suites = getInstance().get().regressionSuites(filter);
        Assert.assertTrue(suites.size() > 0);
        ListAssert.contains(suite, suites);

        filter = new RegressionSuiteFilter();
        filter.estimate.range(12.0, 16.0);
        suites = getInstance().get().regressionSuites(filter);
        Assert.assertTrue(suites.size() > 0);
        ListAssert.contains(suite, suites);

        filter = new RegressionSuiteFilter();
        filter.estimate.range(12.0, 13.0);
        suites = getInstance().get().regressionSuites(filter);
        ListAssert.notcontains(suite, suites);
    }

    @Test
    public void getRegressionSuitesWithReference() {
        final String reference = "TestReference-1010";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Reference", reference);
        RegressionSuite suite = getEntityFactory().createRegressionSuite(RegressionSuiteName, regressionPlan,
                                                                        attributes);

        RegressionSuiteFilter filter = new RegressionSuiteFilter();
        filter.reference.add(reference);
        Collection<RegressionSuite> suites = getInstance().get().regressionSuites(filter);
        Assert.assertTrue(suites.size() > 0);
        ListAssert.contains(suite, suites);

        filter = new RegressionSuiteFilter();
        filter.reference.add("WrongReference");
        suites = getInstance().get().regressionSuites(filter);
        Assert.assertEquals(0, suites.size());
    }

    @Test
    public void getRegressionSuitesByID() {
        RegressionSuite suite = getEntityFactory().createRegressionSuite(RegressionSuiteName, regressionPlan);
        String displayId = suite.getDisplayID();

        RegressionSuiteFilter filter = new RegressionSuiteFilter();
        filter.displayID.add(displayId);
        Collection<RegressionSuite> suites = getInstance().get().regressionSuites(filter);
        Assert.assertEquals(1, suites.size());
        ListAssert.contains(suite, suites);

        filter = new RegressionSuiteFilter();
        filter.displayID.add("WrongNumber");
        suites = getInstance().get().regressionSuites(filter);
        Assert.assertEquals(0, suites.size());
    }
}
