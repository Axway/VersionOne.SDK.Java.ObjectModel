/*(c) Copyright 2012, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("EpicPriority")
public class EpicPriority extends ListValue {
	public EpicPriority(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public EpicPriority(V1Instance instance) {
        super(instance);
    }
}
