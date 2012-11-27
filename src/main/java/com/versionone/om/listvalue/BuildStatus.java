/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("BuildStatus")
public class BuildStatus extends ListValue {
    public BuildStatus(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    public BuildStatus(V1Instance instance) {
        super(instance);
    }
}
