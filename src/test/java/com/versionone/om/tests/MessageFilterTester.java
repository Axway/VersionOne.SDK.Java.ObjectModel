package com.versionone.om.tests;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.versionone.om.Member;
import com.versionone.om.Message;
import com.versionone.om.filters.MessageFilter;

public class MessageFilterTester extends BaseSDKTester {

	String _messageBody = "The message body.";
	private MessageFilter _myMessages = new MessageFilter();

	private Member _me;

	private Member getMe() {
		if (_me == null)
			_me = getInstance().getLoggedInMember();

		return _me;
	}

	@Before
	public void Setup()
	{
		_me = null;
		_myMessages.recipient.add(getMe());

		// Cleanup any old messages for me 
		for(Message myMessage : getInstance().get().messages(_myMessages))
			myMessage.deleteReceipt();

		Assert.assertEquals("I need 0 messages to start for these test to run properly.", 0, getInstance().get().messages(_myMessages).size());

		getInstance().create().message("Basic Message", _messageBody, getMe()).send();
		getInstance().create().message("Read Message", _messageBody, getMe()).send().markAsRead();
		getInstance().create().message("Archived Message", _messageBody, getMe()).send().markAsArchived();
		Message message = getInstance().create().message("Archived Read Message", _messageBody, getMe()).send();
		message.markAsRead();
		message.markAsArchived();

		resetInstance();
	}

	@Test
	public void testAllMessages() {
		MessageFilter filter = new MessageFilter();
		filter.recipient.add(getMe());
		Collection<Message> messages = getInstance().get().messages(filter);
		Assert.assertEquals(4, messages.size());
	}

	@Test
	public void testUnReadMessagesOnly() {
		MessageFilter filter = new MessageFilter();
		filter.recipient.add(getMe());
		filter.isUnread(true);
		Collection<Message> unreadMessages = getInstance().get().messages(filter);
		Assert.assertEquals(2, unreadMessages.size());
	}

	@Test
	public void testReadMessagesOnly() {
		MessageFilter filter = new MessageFilter();
		filter.recipient.add(getMe());
		filter.isUnread(false);
		Collection<Message> readMessages = getInstance().get().messages(filter);
		Assert.assertEquals(2, readMessages.size());
	}

	@Test
	public void testUnArchivedMessagesOnly() {
		MessageFilter filter = new MessageFilter();
		filter.recipient.add(getMe());
		filter.isArchived(false);
		Collection<Message> unarchivedMessages = getInstance().get().messages(filter);
		Assert.assertEquals(2, unarchivedMessages.size());
	}

	@Test
	public void testArchivedMessagesOnly() {
		MessageFilter filter = new MessageFilter();
		filter.recipient.add(getMe());
		filter.isArchived(true);
		Collection<Message> archivedMessages = getInstance().get().messages(filter);
		Assert.assertEquals(2, archivedMessages.size());
	}

	@Test
	public void testArchivedReadMessagesOnly() {
		MessageFilter filter = new MessageFilter();
		filter.recipient.add(getMe());
		filter.isArchived(true);
		filter.isUnread(false);
		Collection<Message> archivedMessages = getInstance().get().messages(filter);
		Assert.assertEquals(1, archivedMessages.size());
	}

}
