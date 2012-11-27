/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Statuses of objects.
 */
@MetaDataAttribute("State")
public class State extends Entity {

    /**
     * future status.
     */
    public static final State FUTURE = new State("State:100");
    /**
     * active status.
     */
    public static final State ACTIVE = new State("State:101");
    /**
     * closed status.
     */
    public static final State CLOSED = new State("State:102");

    private State(String id) {
        this(AssetID.valueOf(id));
    }

    private State(AssetID id) {
        super(id, null);
    }
}
