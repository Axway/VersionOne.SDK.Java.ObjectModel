/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.V1Instance;

public class CustomListValue extends ListValue {

	public CustomListValue(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public CustomListValue(V1Instance instance) {
        super(instance);
    }

    /**
     * The Name.
     *
     * @see com.versionone.om.ListValue#getName().
     */
    @Override
    public String getName() {
        return (String) getCustomListTypeAttribute("Name");
    }

    /**
     * The (optional) Description.
     *
     * @see com.versionone.om.ListValue#getDescription().
     */
    @Override
    public String getDescription() {
        return (String) getCustomListTypeAttribute("Description");
    }

    private Object getCustomListTypeAttribute(String name) {
        return getCustomListTypeAttribute(name, true);
    }

    private Object getCustomListTypeAttribute(String name, boolean cachable) {
        return getInstance().getPropertyOnCustomType(this, name, cachable);
    }
}