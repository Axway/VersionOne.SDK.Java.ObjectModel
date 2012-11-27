/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.Entity;
import com.versionone.om.V1Instance;

/**
 * Represents a List Type Asset.
 */
public abstract class ListValue extends Entity {

    protected ListValue(V1Instance instance) {
        super(instance);
    }

    protected ListValue(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * @return The Name.
     */
    public String getName() {
        return (String) get(NAME_VALUE);
    }

    /**
     * @return The (optional) Description.
     */
    public String getDescription() {
        return (String) get(DESCRIPTION_VALUE);
    }

    /**
     * @return Return name of ListValue.
     */
    @Override
    public String toString() {
        return getName();
    }
}
