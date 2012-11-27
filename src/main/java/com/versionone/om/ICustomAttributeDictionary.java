/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.DB.DateTime;

/**
 * Provides access to custom fields.
 */
public interface ICustomAttributeDictionary {
    /**
     * Get the value of the custom field.
     *
     * @param attributeName The friendly name of the custom field, e.g.
     *                "Origin", not "Custom_Origin".
     * @return custom attribute value.
     */
    Object get(String attributeName);

    /**
     * Sets the value of the custom field.
     *
     * @param attributeName The friendly name of the custom field, e.g.
     *                "Origin", not "Custom_Origin".
     * @param attributevalue custom attribute value.
     */
    void set(String attributeName, Object attributevalue);

    /**
     * Indicates the presence of a custom field.
     *
     * @param attributeName The friendly name of the custom field, e.g.
     *                "Origin", not "Custom_Origin".
     * @return True if the field exists.
     */
    boolean containsKey(String attributeName);

    /**
     * Gets the value of a Custom Field as a numeric.
     *
     * @param attributeName The friendly name of the custom field, e.g.
     *                "Origin", not "Custom_Origin".
     * @return The value of the field as a double, or null.
     */
    Double getNumeric(String attributeName);

    /**
     * Gets the value of a Custom Field as a nullable boolean.
     *
     * @param attributeName The friendly name of the custom field, e.g.
     *                "Origin", not "Custom_Origin".
     * @return The value of the field as a boolean, or null.
     */
    Boolean getBoolean(String attributeName);

    /**
     * Gets the value of a Custom Field as a DateTime.
     *
     * @param attributeName The friendly name of the custom field, e.g.
     *                "Origin", not "Custom_Origin".
     * @return The value of the field as a DateTime, or null.
     */
    DateTime getDate(String attributeName);

    /**
     * Gets the value of a Custom Field as a string.
     *
     * @param attributeName The friendly name of the custom field, e.g.
     *                "Origin", not "Custom_Origin".
     * @return The value of the field as a string, or null.
     */
    String getString(String attributeName);
}