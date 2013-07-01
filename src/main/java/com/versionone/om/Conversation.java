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

    /**
     * @return True if the message can be deleted.
     */
    public boolean canDelete() {
        return getInstance().canExecuteOperation(this, "Delete");
    }

    /**
     * Deletes the conversation.
     * NOTE: only owner can delete expression.
     * @throws UnsupportedOperationException if the item is an invalid state for
     *                                       the Operation.
     */
    public void delete() {
        save();
        getInstance().executeOperation(this, "Delete");
    }
}
