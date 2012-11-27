/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("GoalCategory")
public class GoalType extends ListValue {

	public GoalType(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public GoalType(V1Instance instance) {
        super(instance);
    }
}