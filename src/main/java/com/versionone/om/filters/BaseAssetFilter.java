/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import java.util.ArrayList;
import java.util.List;

import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.TokenTerm;
import com.versionone.om.BaseAsset;
import com.versionone.om.Entity;

/**
 * Filter on BaseAssets.
 */

public class BaseAssetFilter extends EntityFilter {

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

    public Class<? extends Entity> getEntityType() {
        return (BaseAsset.class);
    }

    /**
     * Item name. Must be complete match.
     */
    public final List<String> name = newList();

    /**
     * Item description. Must be complete match.
     */
    public final List<String> description = newList();

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

        builder.simple("Name", name);
        builder.simple("Description", description);
    }

    @Override
    void internalModifyState(FilterBuilder builder) {
		// The basic idea is to exclude 'dead' (Epic'd and Deleted) stuff, exept Epic'd Stories
		// Also take into account the fact that we usually want only active stuff, unless 'Closed' is specified   	
    	
        if (hasState()) {
            builder.root.and(isActive() ? new IFilterTerm[]{new TokenTerm("AssetState='Active'")} :
                    new IFilterTerm[]{new TokenTerm("AssetState='Closed'")});
        } else {
            builder.root.and(new IFilterTerm[]{new TokenTerm("AssetState!='Dead'")});
        }
    }
}
