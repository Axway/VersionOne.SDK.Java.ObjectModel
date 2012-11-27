package com.versionone.om.filters;

import java.util.List;

import com.versionone.om.Entity;
import com.versionone.om.Member;
import com.versionone.om.Message;
import com.versionone.om.MessageReceipt;

public class MessageReceiptFilter extends EntityFilter {

	@Override
	public Class<? extends Entity> getEntityType() {
		return MessageReceipt.class;
	}
	
	/// <summary>
	/// Message receiver.
	/// </summary>
	public final List<Member> recipient = newList();

	/// <summary>
	/// Message itself.
	/// </summary>
	public final List<Message> message = newList();

	@Override 
	void internalModifyFilter(FilterBuilder builder)
	{
		super.internalModifyFilter(builder);

		builder.relation("Message", message);
		builder.relation("Recipient", recipient);
	}
	
}
