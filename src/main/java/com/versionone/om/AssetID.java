/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.util.V1Util;

import java.util.regex.Pattern;

/**
 * Represents a unique ID in the VersionOne system. The ToToken and FromToken
 * methods are provided to convert an AssetID to and from a string for storage.
 */
public class AssetID {
    private final String token;

    /**
     * Construct an AssetID by ID.
     *
     * @param token id of the Asset.
     */
    public AssetID(String token) {
        Pattern regExpToken = Pattern.compile("^[\\w\\d]+\\:\\d+$");

        if (!regExpToken.matcher(token).matches()) {
            throw new IllegalArgumentException(token + " is not AssetId token");
        }

        this.token = token;
    }

    /**
     * @return String representation of the AssetID.
     */
    public String getToken() {
        return token;
    }

    /**
     * Create an AssetID form a tokenized ID.
     *
     * @param token String representation of the AssetID.
     * @return AssetID created by {@code idString}
     */
    public static AssetID fromToken(String token) {
        return new AssetID(token);
    }

    /**
     * Compares this {@code AssetID} with the specified object. The result is
     * {@code true} if and only if the argument is not {@code null} and is an
     * {@code AssetID} with the same {@code token}.
     *
     * @param obj The object to compare this {@code AssetID} against.
     * @return {@code true} if the given object represents the same
     *         {@code AssetID} on the server, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof AssetID)
                && V1Util.equals(token, ((AssetID) obj).token);
    }

    /**
     * To calculate hashCode used {@code token}
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return (token == null) ? 0 : token.hashCode();
    }

    /**
     * Tokenize the ID.
     *
     * @return A string representation of the ID. The same a the Token property.
     */
    @Override
    public String toString() {
        return getToken();
    }

    /**
     * Convert a string to an AssetID.
     *
     * @param idString The tokenized ID.
     * @return AssetID created by {@code idString}.
     */
    public static AssetID valueOf(String idString) {
        return fromToken(idString);
    }
}