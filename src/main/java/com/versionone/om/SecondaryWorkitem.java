/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Represents the base of a Task or Test in the VersionOne System.
 */
@MetaDataAttribute("Workitem")
public abstract class SecondaryWorkitem extends Workitem {

    /**
     * Constructor used to represent an SecondaryWorkitem entity that DOES exist
     * in the VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    SecondaryWorkitem(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an SecondaryWorkitem entity that does NOT
     * yet exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    SecondaryWorkitem(V1Instance instance) {
        super(instance);
    }

    /**
     * @return parent The story, defect or epic this item belongs to.
     */
    public Workitem getParent() {
        return getRelation(Workitem.class, "Parent");
    }

    /**
     * @param parent The story, defect or epic this item belongs to.
     */
    public void setParent(Workitem parent) {
        setRelation("Parent", parent);
    }
}
