/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.Oid;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.apiclient.IMetaModel;
import com.versionone.apiclient.IServices;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.QueryResult;
import com.versionone.apiclient.V1Exception;
import com.versionone.om.AssetID;
import com.versionone.om.Story;

public class EntityCachingTester extends BaseSDKTester {

    private final static String ORIGINAL_NAME = "Original Name";
    private final static String NEW_NAME = "New Name";

    @Test
    public void testGetByID() throws V1Exception {
        Story story = getSandboxProject().createStory(ORIGINAL_NAME);
        AssetID id = story.getID();

        // Make sure Name is in the cache.
        Assert.assertEquals(ORIGINAL_NAME, story.getName());

        // Update story name to "New Name".
        updateStory(id, ORIGINAL_NAME, NEW_NAME);

        // Assert.
        Story story2 = getInstance().get().storyByID(id);
        Assert.assertEquals(NEW_NAME, story2.getName());
    }

    @Test
    public void testGetByID2() throws V1Exception {
        Story story = getSandboxProject().createStory(ORIGINAL_NAME);
        AssetID id = story.getID();

        // Make sure Name is in the cache.
        Assert.assertEquals(ORIGINAL_NAME, story.getName());

        // Update story name to "New Name".
        updateStory(id, ORIGINAL_NAME, NEW_NAME);

        // This should not affect my cached value.
        Assert.assertEquals(ORIGINAL_NAME, story.getName());

        // Assert
        Story story2 = getInstance().get().storyByID(id);
        Assert.assertEquals(NEW_NAME, story2.getName());

        // Both versions should now show the same value.
        Assert.assertEquals(NEW_NAME, story.getName());
    }

    private void updateStory(AssetID id, String originalName, String newName) throws V1Exception {
        IMetaModel metaModel = getInstance().getApiClient().getMetaModel();
        IServices services = getInstance().getApiClient().getServices();

        Oid storyId = Oid.fromToken(id.getToken(), metaModel);
        Query query = new Query(storyId);
        IAssetType storyType = metaModel.getAssetType("Story");
        IAttributeDefinition nameAttribute = storyType
                .getAttributeDefinition("Name");
        query.getSelection().add(nameAttribute);
        QueryResult result = services.retrieve(query);
        Asset storyAsset = result.getAssets()[0];
        String oldName = storyAsset.getAttribute(nameAttribute).getValue()
                .toString();
        Assert.assertEquals(originalName, oldName);
        storyAsset.setAttributeValue(nameAttribute, newName);
        services.save(storyAsset);

        result = services.retrieve(query);
        storyAsset = result.getAssets()[0];
        String newName1 = storyAsset.getAttribute(nameAttribute).getValue()
                .toString();
        Assert.assertEquals(newName, newName1);
    }
}
