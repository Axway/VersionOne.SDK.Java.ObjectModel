package com.versionone.om;

import java.util.Collection;
import java.util.Iterator;

import com.versionone.om.filters.MessageReceiptFilter;

@MetaDataAttribute("Message")
public class Message extends BaseAsset {

	Message(AssetID id, V1Instance instance) {
		super(id, instance);
	}

	Message(V1Instance instance) {
		super(instance);
	}

	/**
	 * Members this message is addressed to. Message may not be sent until there
	 * is at least one recipient.
	 */
	public Collection<Member> getRecipients() {
		return getMultiRelation("Recipients");
	}

	/**
	 * An asset associated with this message.
	 * 
	 * @return BaseAsset
	 */
	@MetaRenamedAttribute("Asset")
	public BaseAsset getRelatedAsset() {
		return getRelation(BaseAsset.class, "Asset");
	}

	public void setRelatedAsset(BaseAsset value) {
		setRelation("Asset", value);
	}

	/**
	 * @return True if the item is in a state in which it may be sent.
	 */
	@MetaRenamedAttribute("CheckSend")
	public boolean isReadyToSend() {
		return (Boolean) get("CheckSend");
	}

	/**
	 * @return True if the item has not been read
	 */
	public boolean isUnread() {
		return getReceipt().isUnread();
	}

	/**
	 * @return True if the item has been read
	 */
	public boolean isRead() {
		return getReceipt().isRead();
	}

	/**
	 * @return True if the item has not been archived
	 */
	public boolean isUnarchived() {
		return getReceipt().isUnarchived();
	}

	/**
	 * @return True if the item has been archived
	 */
	public boolean isArchived() {
		return getReceipt().isArchived();
	}

	/**
	 * Send this message. Must have at least one recipeint. Once sent it may not
	 * be modified. Message will always be saved as part of the send.
	 */
	public Message send() {
		if (getRecipients().size() == 0) {
			throw new MessageException(
					"Must have at least one message recipient to send to.");
		}

		save();

		getInstance().executeOperation(Message.class, this, "Send");

		return this;
	}

	private MessageReceipt _receipt;

	private MessageReceipt getReceipt() {
		if (_receipt == null) {
			MessageReceiptFilter filter = new MessageReceiptFilter();
			filter.recipient.add(getInstance().getLoggedInMember());
			filter.message.add(this);
			Collection<MessageReceipt> receipts = getInstance().get()
					.messageReceipts(filter);
			if (receipts.size() == 1) {
				Iterator<MessageReceipt> iter = receipts.iterator();
				_receipt = iter.next();
			} else {
				throw new NotRecipientofMessageException();
			}
		}

		return _receipt;
	}

	/**
	 * Mark the message as having been read by the recipient.
	 */
	public void markAsRead() {
		getReceipt().markAsRead();
	}

	/**
	 * Mark the message as having not been read by the recipient.
	 */
	public void markAsUnread() {
		getReceipt().markAsUnread();
	}

	/**
	 * Mark the message as having been archived.
	 */
	public void markAsArchived() {
		getReceipt().markAsArchived();
	}

	/**
	 * Mark the message as not having been archived.
	 */
	public void markAsUnarchived() {
		getReceipt().markAsUnarchived();
	}

	@Override
	void closeImpl() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	void reactivateImpl() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Deletes any message receipt associated with this message for the logged
	 * in user.
	 * 
	 * @return true if delete was successful, false otherwise
	 */
	public boolean deleteReceipt() {
		try {
			getReceipt().delete();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
