/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("RequestStatus")
public class RequestStatus extends ListValue {
	public RequestStatus(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public RequestStatus(V1Instance instance) {
        super(instance);
    }
}
