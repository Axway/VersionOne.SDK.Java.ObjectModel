/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("IssueCategory")
public class IssueType extends ListValue {
	public IssueType(AssetID id, V1Instance instance) {
	super(id, instance);
    }

	public IssueType(V1Instance instance) {
	super(instance);
    }
}
