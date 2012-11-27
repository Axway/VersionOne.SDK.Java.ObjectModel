/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * A Story template.
 */
@MetaDataAttribute(value = "Story", assetState = 200)
public class StoryTemplate extends ProjectAsset {
    StoryTemplate(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    StoryTemplate(V1Instance instance) {
        super(instance);
    }

    /**
     * Creates a Story from this Template, copying Attributes and Relationships.
     *
     * @return A Story just like this Template.
     */
    public Story generateStory() {
        save();
        return getInstance().executeOperation(Story.class, this, "Copy");
    }

    void closeImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    void reactivateImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
