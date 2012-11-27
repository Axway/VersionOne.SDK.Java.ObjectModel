/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Base Exception for SDK Exceptions.
 */
public class SDKException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public SDKException(String message) {
        super(message);
    }

    public SDKException(Throwable cause) {
        super(cause);
    }

    public SDKException(String message, Throwable cause) {
        super(message, cause);
    }
}
