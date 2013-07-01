package com.versionone.om;

import com.versionone.DB.DateTime;

import java.util.Collection;

/**
 * Expression entity.
 */
@MetaDataAttribute("Expression")
public class Expression extends Entity {

	Expression(V1Instance instance) {
        super(instance);
    }

	Expression(AssetID id, V1Instance instance) {
        super(id, instance);
    }

	/**
	 * @return Author.
	 */
	public Member getAuthor() {
		return getRelation(Member.class, "Author");
	}

	/**
	 * Sets author for current conversation.
	 * @param member - author
	 */
	public void setAuthor(Member member) {
		setRelation("Author", member);
	}

	/**
	 * @return creation date.
	 */
    public DateTime getAuthoredAt() {
        return new DateTime(get("AuthoredAt"));
    }

    /**
     * Sets date of creation.
     * @param date creation date.
     */
    public void setAuthoredAt(DateTime date) {
    	set("AuthoredAt", date);
    }

    /**
     * @return parent conversation.
     */
    public Conversation getBelongsTo() {
        return getRelation(Conversation.class, "BelongsTo");
    }

    /**
     * Sets parent conversation.
     * @param conversation parent conversation.
     */
    public void setBelongsTo(Conversation conversation) {
    	setRelation("BelongsTo", conversation);
    }

    /**
     * @return The content of this message.;
     */
    public String getContent() {
        return (String)get("Content");
    }

    /**
     * Set content of this message.
     * @param content content for this message.
     */
    public void setContent(String content) {
    	set("Content", content);
    }

    /**
     * @return Parent message.
     */
    public Conversation getInReplyTo() {
        return getRelation(Conversation.class, "InReplyTo");
    }

    /**
     * Sets parent message to current.
     * @param parent parent message.
     */
    public void setInReplyTo(Expression parent) {
    	setRelation("InReplyTo", parent);
    }

    /**
     * @return Replies posted to current item.
     */
    public Collection<Expression> getReplies() {
        return getMultiRelation("Replies");
    }

    /**
     * @return Mentioned members.
     */
    public Collection<BaseAsset> getMentions() {
        return getMultiRelation("Mentions");
    }

    /**
     * @return True if the message can be deleted.
     */
	public boolean canDelete() {
		return getInstance().canExecuteOperation(this, "Delete");
	}

	/**
	 * Deletes the expression.
	 * NOTE: only owner can delete expression.
     * @throws UnsupportedOperationException if the item is an invalid state for
     *                                       the Operation.
	 */
	public void delete() {
		save();
		getInstance().executeOperation(this, "Delete");
	}

}
