/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.om.AssetID;
import com.versionone.om.Epic;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Link;
import com.versionone.om.Project;

import java.util.HashMap;
import java.util.Map;

public class LinkTester extends BaseSDKTester {
    @Test
    public void testLinkAttributes() {
        Link link = getInstance().get().linkByID("Link:1780");
        Project project = getInstance().get().projectByID(SCOPE_ZERO);
        Assert.assertEquals(project, link.getAsset());
        Assert.assertEquals("Link A", link.getName());
        Assert.assertTrue(link.isOnMenu());
        Assert.assertEquals("http://www.google.com/?q=A", link.getURL());
    }

    private static final String LINK_Y = "Link Y";
    private static final String LINK_Z = "Link Z";
    private static final String LINK_Y_URL = "http://www.google.com?q=Y";
    private static final String LINK_Z_URL = "http://www.google.com?q=Z";

    @Test
    public void testCreate() {
        Project project = getInstance().get().projectByID(SCOPE_ZERO);
        Link linkY = project.createLink(LINK_Y, LINK_Y_URL, false);
        Link linkZ = project.createLink(LINK_Z, LINK_Z_URL, true);

        String linkYid = linkY.getID().toString();
        String linkZid = linkZ.getID().toString();
        resetInstance();

        Link newLinkY = getInstance().get().linkByID(linkYid);
        Assert.assertEquals(LINK_Y, newLinkY.getName());
        Assert.assertEquals(LINK_Y_URL, newLinkY.getURL());
        Assert.assertFalse(newLinkY.isOnMenu());

        Link newLinkZ = getInstance().get().linkByID(linkZid);
        Assert.assertEquals(LINK_Z, newLinkZ.getName());
        Assert.assertEquals(LINK_Z_URL, newLinkZ.getURL());
        Assert.assertTrue(newLinkZ.isOnMenu());
    }

    @Test
    public void testCreateLinkWithAttributes() {
        final String name = "LinkName";
        final String url = "www.testURL.net";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("URL", url);

        Link link = getSandboxProject().createLink(name, "BadUrl", true, attributes);
        AssetID epicID = link.getID();

        resetInstance();

        link = getInstance().get().linkByID(epicID);

        Assert.assertEquals(name, link.getName());
        Assert.assertEquals(url, link.getURL());

        link.delete();
    }

    private static final String LINK_1782 = "Link:1782";

    @Test
    public void testDelete() {
        Link link = getInstance().get().linkByID(LINK_1782);
        Assert.assertNotNull(link);
        Assert.assertTrue(link.canDelete());
        link.delete();
        resetInstance();
        Assert.assertNull(getInstance().get().linkByID(LINK_1782));
    }
}
