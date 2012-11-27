/*(c) Copyright 2012, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("EpicCategory")
public class EpicType extends ListValue {
	public EpicType(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public EpicType(V1Instance instance) {
        super(instance);
    }
}
