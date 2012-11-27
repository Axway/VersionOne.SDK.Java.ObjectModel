/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Represents a Link in the VersionOne system.
 */
@MetaDataAttribute("Link")
public class Link extends Entity {

    Link(V1Instance instance) {
        super(instance);
    }

    Link(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * @return Asset this link is related to.
     */
    public BaseAsset getAsset() {
        return getRelation(BaseAsset.class, "Asset");
    }

    /**
     * @param value Asset this link is related to.
     */
    public void setAsset(BaseAsset value) {
        setRelation("Asset", value);
    }

    /**
     * @return True if this link is visible on the asset's detail page menu.
     */
    public boolean isOnMenu() {
        return (Boolean) get("OnMenu");
    }

    /**
     * @param value True if this link is visible on the asset's detail page
     *                menu.
     */
    public void setOnMenu(boolean value) {
        set("OnMenu", value);
    }

    /**
     * @return URL this link points to.
     */
    public String getURL() {
        return (String) get("URL");
    }

    /**
     * @param value URL this link points to.
     */
    public void setURL(String value) {
        set("URL", value);
    }

    /**
     * @return Name of this link.
     */
    public String getName() {
        return (String) get(NAME_VALUE);
    }

    /**
     * @param value Name of this link.
     */
    public void setName(String value) {
        set(NAME_VALUE, value);
    }

    /**
     * @return True if this link can be deleted.
     */
    public boolean canDelete() {
        return getInstance().canExecuteOperation(this, DELETE_OPERATION);
    }

    /**
     * Deletes the link.
     *
     * @throws UnsupportedOperationException if the item is an invalid state for
     *                 the Operation.
     */
    public void delete() throws UnsupportedOperationException {
        save();
        getInstance().executeOperation(this, DELETE_OPERATION);
    }
}
