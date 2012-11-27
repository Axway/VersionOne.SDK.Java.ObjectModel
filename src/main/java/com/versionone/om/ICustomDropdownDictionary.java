/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Provides access to Custom List-Type values.
 */
public interface ICustomDropdownDictionary {

    /**
     * Gets the value of a custom relation.
     * 
     * @param attributeName The friendly name of the custom relation, e.g.
     *                "Origin", not "Custom_Origin".
     * @return the {@code IListValueProperty} object by the
     *         {@code attributeName}
     */
    IListValueProperty get(String attributeName);

    /**
     * Indicates the presence of a custom relation.
     * 
     * @param attributeName The friendly name of the custom relation, e.g.
     *                "Origin", not "Custom_Origin".
     * @return True if the custom relation exists.
     */
    boolean containsKey(String attributeName);
}
