package com.versionone.om;

/**
 * The operation or attribute is only avalable when the logged in user is a recipient of the message.
 */
public class NotRecipientofMessageException extends MessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Basic constructor.
	 */
	public NotRecipientofMessageException() {
		super("Must be a recipient of the message to perform operation.");	
	}
}
