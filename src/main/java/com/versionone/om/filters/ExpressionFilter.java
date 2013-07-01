package com.versionone.om.filters;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter for getting Expression.
 */
public class ExpressionFilter extends EntityFilter {

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
		return Expression.class;
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
    public List<Conversation> belongsTo = newList();

    /**
     * Filter on the ExpressionsInConversation property.
     */
    //public List<Expression> expressionsInConversation = newList();

    /**
     * Filter on the InReplyTo property.
     */
    public List<Expression> inReplyTo = newList();

    /**
     * Filter on the Replies property.
     */
    public List<Expression> replies = newList();

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
        builder.relation("BelongsTo", belongsTo);
        //builder.multiRelation("ExpressionsInConversation", expressionsInConversation);
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
