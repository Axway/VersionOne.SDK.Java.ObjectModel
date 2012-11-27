/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("WorkitemPriority")
public class WorkitemPriority extends ListValue {

	public WorkitemPriority(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public WorkitemPriority(V1Instance instance) {
        super(instance);
    }
}
