/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Represents a List Value property of an Entity.
 */
public interface IListValueProperty {
    /**
     * @return the currently saved value.
     */
    String getCurrentValue();

    /**
     * @param value to set as current.
     */
    void setCurrentValue(String value);

    /**
     * @return The names of all of the active values that this relation can be
     *         set to.
     */
    String[] getAllValues();

    /**
     * Validates a value for this relation.
     *
     * @param value The name to validate.
     * @return True if the name matches an item in the list, otherwise false.
     */
    boolean isValid(String value);

    /**
     * Removes the currently selected value.
     */
    void clearCurrentValue();
}