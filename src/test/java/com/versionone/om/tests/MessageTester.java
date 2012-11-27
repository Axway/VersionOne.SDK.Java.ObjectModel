package com.versionone.om.tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.versionone.om.DataException;
import com.versionone.om.Member;
import com.versionone.om.Message;
import com.versionone.om.MessageException;
import com.versionone.om.NotRecipientofMessageException;
import com.versionone.om.Story;

public class MessageTester extends BaseSDKTester {
	private String messageName = "Test Message from SDK";
	String messageBody = "The message body.";

	@Test
	public void testCreate() {
		Message message = getInstance().create().message(messageName, messageBody, getInstance().getLoggedInMember());

		Assert.assertEquals(messageName, message.getName());
		Assert.assertEquals(1, message.getRecipients().size());
		Iterator<Member> recipient = message.getRecipients().iterator();
		Assert.assertEquals(getInstance().getLoggedInMember(), recipient.next());
	}

    @Test
	public void testCreateMessageWithAttributes() {
        final String messageName = "MessageName";
        final String description = "Test for Message creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

		Message message = getInstance().create().message(messageName, messageBody, attributes);

		Assert.assertEquals(messageName, message.getName());
		Assert.assertEquals(description, message.getDescription());

        message.delete();
	}

	@Test
	public void testCreateWithRelatedAsset() {
		Message message = getInstance().create().message(messageName, messageBody, getInstance().getLoggedInMember());

		Story story = getInstance().create().story("Story with Message", getSandboxProject());

		message.setRelatedAsset(story);
		message.save();

		Assert.assertEquals(story, message.getRelatedAsset());
	}

	@Test
	public void testNotReadyToSend() {
		Message message = getInstance().create().message(messageName, messageBody);
		Assert.assertEquals(false, message.isReadyToSend());
	}

	@Test
	public void testReadyToSend() {
		Message message = getInstance().create().message(messageName, messageBody, getInstance().getLoggedInMember());
		Assert.assertEquals(true, message.isReadyToSend());
	}

	@Test
	public void testNewMessageIsUnread() {
		Message message = getInstance().create().message(messageName, messageBody, getInstance().getLoggedInMember());
		message.send();

		Assert.assertTrue(message.isUnread());
		Assert.assertFalse(message.isRead());
	}

	@Test
	public void testMarkMessageRead() {
		Message message = getInstance().create().message("Read Message", messageBody, getInstance().getLoggedInMember());
		message.send();

		message.markAsRead();

		Assert.assertFalse(message.isUnread());
		Assert.assertTrue(message.isRead());
		Assert.assertTrue("Marking message read should not archive the message.", message.isUnarchived());
	}

	@Test
	public void testMarkMessageUnread() {
		Message message = getInstance().create().message(messageName, messageBody, getInstance().getLoggedInMember());
		message.send();

		message.markAsRead();
		message.markAsUnread();

		Assert.assertTrue(message.isUnread());
		Assert.assertFalse(message.isRead());
	}

	@Test
	public void testNewMessageIsUnarchived() {
		Message message = getInstance().create().message(messageName, messageBody, getInstance().getLoggedInMember());
		message.send();

		Assert.assertTrue(message.isUnarchived());
		Assert.assertFalse(message.isArchived());
	}

	@Test
	public void testArchiveMessage() {
		Message message = getInstance().create().message("Archived Message", messageBody, getInstance().getLoggedInMember());
		message.send();

		message.markAsArchived();

		Assert.assertFalse(message.isUnarchived());
		Assert.assertTrue(message.isArchived());
		Assert.assertTrue("Marking message archived should not mark the message as read.", message.isUnread());
	}

	@Test
	public void testUnarchivedMessage() {
		Message message = getInstance().create().message(messageName, messageBody, getInstance().getLoggedInMember());
		message.send();

		message.markAsArchived();
		message.markAsUnarchived();

		Assert.assertTrue(message.isUnarchived());
		Assert.assertFalse(message.isArchived());
	}

	@Test
	public void testDeleteMyOwnMessageReceipt() {
		Message message = getInstance().create().message("Deleted Message", messageBody, getInstance().getLoggedInMember());
		message.send();

		Assert.assertTrue(message.deleteReceipt());
	}

	@Test
	public void testCannotDeleteOthersMessageReceipt() {
		Message message = getInstance().create().message("Deleted Message", messageBody, getSandboxMember());
		message.send();

		Assert.assertFalse(message.deleteReceipt());
	}

	@Test(expected=MessageException.class)
	public void testSendMessageWithNoRecipients() {
		Message message = getInstance().create().message(messageName, messageBody, (Collection<Member>)null);
		message.send();
	}

	@Test(expected=NotRecipientofMessageException.class)
	public void testMarkSomeoneElseMessageAsRead() {
		Message message = getInstance().create().message(messageName, messageBody, getSandboxMember());
		message.send();
		message.markAsRead();
	}

	@Test(expected=DataException.class)
	public void testEditSentMessage() {
		Message message = getInstance().create().message(messageName, messageBody, getSandboxMember());
		message.send();
		message.setDescription("New description");
		message.save();
	}
}
