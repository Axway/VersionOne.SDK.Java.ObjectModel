/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("TaskStatus")
public class TaskStatus extends ListValue {
	public TaskStatus(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public TaskStatus(V1Instance instance) {
        super(instance);
    }
}
