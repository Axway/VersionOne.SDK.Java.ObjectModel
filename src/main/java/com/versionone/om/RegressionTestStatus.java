package com.versionone.om;

import com.versionone.om.listvalue.ListValue;

@MetaDataAttribute("RegressionTestStatus")
public class RegressionTestStatus extends ListValue {

    public RegressionTestStatus(V1Instance instance) {
        super(instance);
    }

    public RegressionTestStatus(AssetID id, V1Instance instance) {
        super(id, instance);
    }
}
