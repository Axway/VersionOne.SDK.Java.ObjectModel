/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("TaskCategory")
public class TaskType extends ListValue {
	public TaskType(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public TaskType(V1Instance instance) {
        super(instance);
    }
}
