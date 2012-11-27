/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Throw when an instance is validated and the supplied credentials are invalid.
 */
public class AuthenticationException extends SDKException {
    private static final long serialVersionUID = 1L;

    AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
