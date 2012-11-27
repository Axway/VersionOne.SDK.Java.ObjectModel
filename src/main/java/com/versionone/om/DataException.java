/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Thrown when an entity is saved and a rule or security violation has occurred.
 */
public class DataException extends SDKException {
    private static final long serialVersionUID = 1L;

    DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
