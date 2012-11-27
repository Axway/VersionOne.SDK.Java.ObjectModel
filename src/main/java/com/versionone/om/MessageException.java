package com.versionone.om;

/**
 * An exception with a message.
 */
public class MessageException extends SDKException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Basic constructor.
	 * @param message - error message
	 */
	public MessageException(String message) 
	{
		super(message);
	}
}
