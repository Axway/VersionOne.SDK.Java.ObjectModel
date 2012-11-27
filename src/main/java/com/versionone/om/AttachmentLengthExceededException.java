/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * Thrown when an attachment stream exceeds the server's allowed size limit.
 */
public class AttachmentLengthExceededException extends SDKException {

    private static final long serialVersionUID = 1L;

    /**
     * Creating exception with message of error.
     *
     * @param message text of the error.
     */
    public AttachmentLengthExceededException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause the cause (which is saved for later retrieval by the {@link
     *                #getCause()} method). (A null value is permitted, and
     *                indicates that the cause is nonexistent or unknown.)
     */
    public AttachmentLengthExceededException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by
     *                the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method). (A null value is permitted,
     *                and indicates that the cause is nonexistent or unknown.)
     */
    public AttachmentLengthExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
