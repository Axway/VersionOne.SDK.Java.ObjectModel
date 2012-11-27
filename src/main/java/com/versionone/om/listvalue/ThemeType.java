/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.listvalue;

import com.versionone.om.AssetID;
import com.versionone.om.MetaDataAttribute;
import com.versionone.om.V1Instance;

@MetaDataAttribute("ThemeCategory")
public class ThemeType extends ListValue {

	public ThemeType(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	public ThemeType(V1Instance instance) {
        super(instance);
    }
}
