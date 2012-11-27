package com.versionone.om.filters;

import java.util.ArrayList;
import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.BaseAsset;
import com.versionone.om.Conversation;
import com.versionone.om.Entity;
import com.versionone.om.Member;

/**
 * Filter for getting Expression/Conversation.
 */
public class ConversationFilter extends EntityFilter {

    /**
     * State of the asset.
     */
    public enum State {
        /**
         * Active or open.
         */
        Active,
        /**
         * Closed or inactive.
         */
        Closed
    }

    private final List<State> state = new ArrayList<State>(2);

	@Override
	public Class<? extends Entity> getEntityType() {
		return Conversation.class;
	}

	/**
	 * Filter on the Author property.
	 */
    public List<Member> author = newList();

    /**
     * Filter on the AuthoredAt property.
     */
    public DateSearcher authoredAt = new DateSearcher();

    /**
     * Filter on the Conversation property.
     */
    public List<Conversation> conversation = newList();

    /**
     * Filter on the ExpressionsInConversation property.
     */
    public List<Conversation> expressionsInConversation = newList();

    /**
     * Filter on the InReplyTo property.
     */
    public List<Conversation> inReplyTo = newList();

    /**
     * Filter on the Replies property.
     */
    public List<Conversation> replies = newList();

    /**
     * Filter on the Mentions property.
     */
    public List<BaseAsset> mentions = newList();

    /**
     * @return Current State of the asset.
     */
    public List<State> getState() {
        return state;
    }

    boolean hasState() {
        return getState().size() == 1;
    }

    boolean isActive() {
        return getState().contains(State.Active);
    }

    boolean isClosed() {
        return getState().contains(State.Closed);
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        builder.relation("Author", author);
        builder.comparison("AuthoredAt", authoredAt);
        builder.relation("Conversation", conversation);
        builder.multiRelation("ExpressionsInConversation", expressionsInConversation);
        builder.relation("InReplyTo", inReplyTo);
        builder.multiRelation("Replies", replies);
        builder.multiRelation("Mentions", mentions);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
        if (hasState()) {
            if (isActive()) {
                builder.root.and(new IFilterTerm[]{new TokenTerm("AssetState='Active';AssetType='Expression'")});
            } else {
                builder.root.and(new IFilterTerm[]{new TokenTerm("AssetState='Closed';AssetType='Expression'")});
            }
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm("AssetState!='Dead';AssetType='Expression'")});
        }
    }
}
