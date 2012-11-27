/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Thrown when an instance is validated and cannot communicate with the
 * VersionOne Application.
 */
public class ApplicationUnavailableException extends SDKException {
    static final long serialVersionUID = 1L;

    ApplicationUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    ApplicationUnavailableException(Throwable cause) {
        super(cause);
    }
}
