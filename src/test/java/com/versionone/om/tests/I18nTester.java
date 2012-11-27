/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.om.AssetID;
import com.versionone.om.Story;

import org.junit.Assert;
import org.junit.Test;

public class I18nTester extends BaseSDKTester {

    @Test
    public void testUnicodeStoryName() {
        final String name = "\u0420\u0443\u0441 - Русская история";
        final AssetID id = getInstance().create().story(name, getSandboxProject()).getID();
        resetInstance();

        Story story = getInstance().get().storyByID(id);
        Assert.assertEquals(name, story.getName());
        story.delete();
        getSandboxProject().delete();
    }
}