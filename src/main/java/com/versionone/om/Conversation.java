package com.versionone.om;

import java.util.Collection;

/**
 * Conversation entity.
 */
@MetaDataAttribute("Conversation")
public class Conversation extends Entity {
    Conversation(V1Instance instance) {
        super(instance);
    }

    Conversation(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * @return Child expressions belonging to current conversation.
     */
    public Collection<Expression> getContainedExpressions() {
        return getMultiRelation("ContainedExpressions");
    }
}
