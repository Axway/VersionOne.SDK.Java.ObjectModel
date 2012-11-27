/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.filters.ChangeSetFilter;

public class ChangeSetFilterTester extends BaseSDKTester {

    @Test
    public void testReference() {
        String reference = newGuid();
        getInstance().create().changeSet("Test", reference);
        resetInstance();
        ChangeSetFilter filter = new ChangeSetFilter();
        filter.reference.add(reference);
        Assert.assertEquals(1, getInstance().get().changeSets(filter)
                .size());
    }
}
