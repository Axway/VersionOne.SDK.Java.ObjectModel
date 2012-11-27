/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.om.Team;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.AssetID;
import com.versionone.om.TestSuite;

import java.util.HashMap;
import java.util.Map;

public class TestSuiteTester extends BaseSDKTester {

    @Test
    public void testCreateAndRetrieveTestSuite() {
        final String suiteName = "The Suite of Tests";
        final String reference = "ABC123";

        AssetID id = getInstance().create().
                testSuite(suiteName, reference).getID();
        resetInstance();

        TestSuite testSuite = getInstance().get().testSuiteByID(id);
        Assert.assertEquals(testSuite.getName(), suiteName);
        Assert.assertEquals(testSuite.getReference(), reference);
        Assert.assertTrue(
                "Expected to find newly saved TestSuite in the enumerable of " +
                        "TestSuites on V1Instance.",
                findRelated(testSuite, getInstance().getTestSuites()));

        Assert.assertNull(testSuite.getURL());
    }

    @Test
    public void testCreateTestSuiteWithAttributes() {
        final String name = "TestSuiteName";
        final String reference = "ABC123";
        final String description = "Test for TestSuite creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        AssetID id = getInstance().create().testSuite(name, reference, attributes).getID();
        resetInstance();

        TestSuite testSuite = getInstance().get().testSuiteByID(id);

        Assert.assertEquals(name, testSuite.getName());
        Assert.assertEquals(description, testSuite.getDescription());

        testSuite.delete();
    }
}
