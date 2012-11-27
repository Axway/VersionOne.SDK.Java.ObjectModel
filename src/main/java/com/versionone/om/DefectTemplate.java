/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * A Defect template.
 */
@MetaDataAttribute(value = "Defect", assetState = 200)
public class DefectTemplate extends ProjectAsset {

    /**
     * Constructor used to represent an DefectTemplate entity that DOES exist in
     * the VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    DefectTemplate(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an DefectTemplate entity that does NOT yet
     * exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    DefectTemplate(V1Instance instance) {
        super(instance);
    }

    /**
     * Creates a Defect from this Template, copying Attributes and
     * Relationships.
     *
     * @return A Defect just like this Template.
     */
    public Defect generateDefect() {
        save();
        return getInstance().executeOperation(Defect.class, this, "Copy");
    }

    /**
     * Closes the ChangeSet.
     *
     * @throws UnsupportedOperationException throws always because of
     *                 DefectTemplate cannot be closed in VersionOne.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "DefectTemplate cannot be closed in VersionOne");
    }

    /**
     * Reopens the ChangeSet.
     *
     * @throws UnsupportedOperationException throws always because of
     *                 DefectTemplate cannot be reactivated in VersionOne.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "DefectTemplate cannot be reactivated in VersionOne");
    }
}
