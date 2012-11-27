package com.versionone.om.tests;

import com.versionone.om.Member;
import com.versionone.om.Project;
import com.versionone.om.RegressionTest;
import com.versionone.om.filters.RegressionTestFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;

public class RegressionTestFilterTester extends BaseSDKTester {
    private final String RegressionTestName = "My Regression Test";

    @Override
    protected Project createSandboxProject(Project rootProject) {
        return getEntityFactory().createProjectWithSchedule(getSandboxName(), rootProject);
    }

    @After
    public void tearDown() {
        newSandboxProject();
    }

    @Test
    public void getRegressionTestWithNullFilter() {
        Collection<RegressionTest> tests = getInstance().get().regressionTests(null);
        RegressionTest newTest = getEntityFactory().createRegressionTest(RegressionTestName, getSandboxProject());
        resetInstance();

        Collection<RegressionTest> updatedTests = getInstance().get().regressionTests(null);

        Assert.assertEquals(1, updatedTests.size() - tests.size());
        ListAssert.contains(newTest, updatedTests);
        ListAssert.notcontains(newTest, tests);
    }

    @Test
    public void getRegressionTestByOwner() {
        Member firstUser = getEntityFactory().createMember("test user");
        Member secondUser = getEntityFactory().createMember("second user");
        RegressionTest test = getEntityFactory().createRegressionTest(RegressionTestName, getSandboxProject());
        test.getOwners().add(firstUser);
        test.save();

        resetInstance();

        RegressionTestFilter filter = new RegressionTestFilter();
        filter.owners.add(secondUser);
        Collection<RegressionTest> tests = getInstance().get().regressionTests(filter);
        ListAssert.notcontains(test, tests);

        filter.owners.clear();
        filter.owners.add(firstUser);
        tests = getInstance().get().regressionTests(filter);
        ListAssert.contains(test, tests);
    }

    @Test
    public void getRegressionTestByTags() {
        String matchingTag = "match";
        String nonMatchingTag = "wrong";

        RegressionTest test = getEntityFactory().createRegressionTest(RegressionTestName, getSandboxProject());
        test.setTags(matchingTag);
        test.save();

        resetInstance();

        RegressionTestFilter filter = new RegressionTestFilter();
        filter.tags.add(nonMatchingTag);
        Collection<RegressionTest> tests = getInstance().get().regressionTests(filter);
        ListAssert.notcontains(test, tests);

        filter.tags.clear();
        filter.tags.add(matchingTag);
        tests = getInstance().get().regressionTests(filter);
        ListAssert.contains(test, tests);
    }

    @Test
    public void getRegressionTestByReference() {
        String matchingReference = "match";
        String nonMatchingReference = "wrong";

        RegressionTest test = getEntityFactory().createRegressionTest(RegressionTestName, getSandboxProject());
        test.setReference(matchingReference);
        test.save();

        resetInstance();

        RegressionTestFilter filter = new RegressionTestFilter();
        filter.reference.add(nonMatchingReference);
        Collection<RegressionTest> tests = getInstance().get().regressionTests(filter);
        ListAssert.notcontains(test, tests);

        filter.reference.clear();
        filter.reference.add(matchingReference);
        tests = getInstance().get().regressionTests(filter);
        ListAssert.contains(test, tests);
    }

    @Test
    @Ignore("There are no statuses configured for Regression Tests by default, " +
            "so we cannot make it reliable and non-redundant.")
    public void getRegressionTestByStatus() {
        RegressionTest test = getEntityFactory().createRegressionTest(RegressionTestName, getSandboxProject());
        String status = test.getStatus().getAllValues()[0];
        String wrongStatus = test.getStatus().getAllValues()[1];
        test.getStatus().setCurrentValue(status);
        test.save();

        resetInstance();

        RegressionTestFilter filter = new RegressionTestFilter();
        filter.status.add(wrongStatus);
        Collection<RegressionTest> tests = getInstance().get().regressionTests(filter);
        ListAssert.notcontains(test, tests);

        filter.status.clear();
        filter.status.add(status);
        tests = getInstance().get().regressionTests(filter);
        ListAssert.contains(test, tests);
    }

    @Test
    public void getRegressionTestByType() {
        RegressionTest test = getEntityFactory().createRegressionTest(RegressionTestName, getSandboxProject());
        String type = test.getType().getAllValues()[0];
        String wrongType = test.getType().getAllValues()[1];
        test.getType().setCurrentValue(type);
        test.save();

        resetInstance();

        RegressionTestFilter filter = new RegressionTestFilter();
        filter.type.add(wrongType);
        Collection<RegressionTest> tests = getInstance().get().regressionTests(filter);
        ListAssert.notcontains(test, tests);

        filter.type.clear();
        filter.type.add(type);
        tests = getInstance().get().regressionTests(filter);
        ListAssert.contains(test, tests);
    }
}
