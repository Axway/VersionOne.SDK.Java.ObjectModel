/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Class for ordering the Assets.
 *
 * @param <T> The type of asset being ranked.
 */
public class Rank<T extends Entity> {

    private final T asset;
    private final String rankAttribute;
    private final V1Instance instance;

    Rank(V1Instance instance, T asset, String rankAttribute) {
        this.instance = instance;
        this.asset = asset;
        this.rankAttribute = rankAttribute;
    }

    /**
     * Set this Entity ahead of the passed in Entity in rank order.
     *
     * @param assetToRankAheadOf The Entity that will come next in order after
     *                this Entity.
     */
    public void setAbove(T assetToRankAheadOf) {
        instance.rankAbove(asset, assetToRankAheadOf, rankAttribute);
        asset.save();
    }

    /**
     * Set this Entity after the passed in Entity in rank order.
     *
     * @param assetToRankAfter The Entity that will come just before this Entity
     *                in rank order.
     */
    public void setBelow(T assetToRankAfter) {
        instance.rankBelow(asset, assetToRankAfter, rankAttribute);
        asset.save();
    }

    /**
     * Determines whether otherAsset is above this one.
     *
     * @param otherAsset The Entity that will come next in order after this
     *                Entity.
     * @return true if this Entity ahead of the passed in Entity in rank order,
     *         false otherwise.
     */
    public boolean isAbove(T otherAsset) {
        return instance.isRankAbove(asset, otherAsset, rankAttribute);
    }

    /**
     * Determines whether otherAsset is above this one.
     *
     * @param otherAsset The Entity that will come just before this Entity in
     *                rank order.
     * @return true if this Entity after the passed in Entity in rank order,
     *         false otherwise.
     */
    public boolean isBelow(T otherAsset) {
        return instance.isRankBelow(asset, otherAsset, rankAttribute);
    }
}
