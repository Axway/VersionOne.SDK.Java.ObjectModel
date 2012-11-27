/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;


import java.util.UUID;

class StubAssetID {
    private final UUID id = UUID.randomUUID();

    /**
     * Compare this StubAssetID to another, and return true if they are the same
     * identifier.
     *
     * @param obj other {@code Guid} object.
     * @return true if they are the same identifier.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof StubAssetID) {
            return id.equals(((StubAssetID) obj).id);
        }
        return false;
    }

    /**
     * @return hashcode of Object.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * @return string representation of this StubAssetID.
     */
    @Override
    public String toString() {
        return id.toString();
    }
}